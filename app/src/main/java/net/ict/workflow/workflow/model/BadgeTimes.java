package net.ict.workflow.workflow.model;

import android.content.Context;
import android.widget.Toast;

import com.google.common.collect.TreeRangeSet;

import net.ict.workflow.workflow.Converter;
import net.ict.workflow.workflow.R;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class BadgeTimes {
    TreeSet<LocalDateTime> times;

    private static final String TAG = "MyActivity";
    private static final int WORK_DAYS = 5;
    private DatabaseHelperInterface dbh;
    private Context context;

    public BadgeTimes() {
        dbh = new DatabaseHelperStub();
        times = dbh.getAllBadgeTimes();
    }

    public BadgeTimes(Context context) {
        dbh = new DatabaseHelper(context);
        this.context = context;
        times = dbh.getAllBadgeTimes();
        //times = new TreeSet<>();
    }

    public ArrayList<LocalDateTime> getTimeStampsInDate(LocalDate date) {
        ArrayList<LocalDateTime> result = new ArrayList<>();
        Iterator<LocalDateTime> iter = times.iterator();
        while (iter.hasNext()) {
            LocalDateTime ts = iter.next();
            boolean sameDay = (date.getDayOfYear()==ts.getDayOfYear() && date.getYear()==ts.getYear());
            if (sameDay) {
                result.add(ts);
            }
        }
        return result;
    }
    /**
     * @deprecated use {@link #getBadgedTime(CardType ct, LocalDateTime ldt)} instead.
     */
    @Deprecated
    public float getBadgedTimeDay(LocalDateTime date) {
        LocalDate startDate = date.toLocalDate();
        long daysBetween = 1;
        float max = getSecondsBetweenDays(daysBetween, startDate);
        return max;
    }

    /**
     * @deprecated use {@link #getBadgedTime(CardType ct, LocalDateTime ldt)} instead.
     */
    @Deprecated
    public float getBadgedTimeWeek(LocalDateTime date) {
        LocalDate startDate = date.with(DayOfWeek.MONDAY).toLocalDate();
        long daysBetween = 7;
        float max = getSecondsBetweenDays(daysBetween, startDate);
        return max;
    }

    /**
     * @deprecated use {@link #getBadgedTime(CardType ct, LocalDateTime ldt)} instead.
     */
    @Deprecated
    public float getBadgedTimeMonth(LocalDateTime date) {
        LocalDate startDate = date.withDayOfMonth(1).toLocalDate();
        long daysBetween = startDate.lengthOfMonth();
        float max = getSecondsBetweenDays(daysBetween, startDate);
        return max;
    }

    public float getSecondsBetweenDays(long daysBetween, LocalDate startDate) {
        long times = 0;
        for(int i = 0; i < daysBetween; i++){
            ArrayList<LocalDateTime> dates = getTimeStampsInDate(startDate.plusDays(i));
            LocalDateTime current = null;
            Boolean pauseOrWork = true;
            for (LocalDateTime ldt : dates) {
                if (current==null) {
                    current = ldt;
                } else {
                    if (pauseOrWork) {
                        times = times + (ChronoUnit.SECONDS.between(current, ldt));
                        pauseOrWork = false;
                    } else {
                        pauseOrWork = true;
                        current = ldt;
                    }
                }
            }
        }
        return (float) times/((float)(60*60));
    }

    public LocalDateTime badgedTimesEven() {
        if (times.size()%2==1) {
            return searchForUnevenTime();
        }
        return null;
    }

    private LocalDateTime searchForUnevenTime() {
        LocalDateTime ldt = this.times.first();
        while (true) {
            ArrayList<LocalDateTime> arrLdt = getTimeStampsInDate(ldt.toLocalDate());
            if (arrLdt.size()%2==1) {
                return arrLdt.get(0);
            } else {
                ldt = this.times.higher(arrLdt.get(arrLdt.size()-1));
            }
        }

    }

    public float getMax(CardType type, LocalDateTime[] ldt, LocalDateTime ldtSuperbe) {
        float max = 0;
        float perDay = OwnSettings.getTimePerDay();
        Boolean[] daysToWork = OwnSettings.getWeeks();

        if (ldt == null) {

                switch(type){
                    case DAY:
                        if (daysToWork[ldtSuperbe.getDayOfWeek().getValue()-1]) {
                            return perDay;
                        } else {
                            return 0;
                        }
                    case WEEK:
                        return perDay * workingDays();
                    case MONTH:
                        return perDay * getDaysOfMonth(ldtSuperbe, daysToWork);
                    default:
                        return perDay;
            }
        } else {
            int i = 0;
            for (LocalDateTime dow : ldt) {
                if (dow == null) {
                    if (daysToWork[i%7]) {
                        max = max + perDay;
                    }
                } else {
                    max = max + dbh.getBadgeTimeMax(dow);
                }
                i++;
            }

        }
        return max;
    }

    private long workingDays() {
        Boolean[] weekDays = Converter.getWeekDays();
        int lauf = 0;
        for (Boolean bu : weekDays) {
            if (bu) {
                lauf++;
            }
        }
        return lauf;
    }

    private int getDaysOfMonth(LocalDateTime ldt, Boolean[] daysToWork){
        int count = 0;
        LocalDateTime local = ldt.withDayOfMonth(1);
        LocalDateTime local2 = local;

        while (local.getMonth()==local2.getMonth()) {
            if (daysToWork[local2.getDayOfWeek().getValue()-1]) {
                count++;
            }
            local2 = local2.plusDays(1);
        }

        return count;
    }



    public void addBadgeTime(LocalDateTime ldt, int daysCode) {
        if (OwnSettings.getWeeks()[ldt.getDayOfWeek().getValue()-1]) {
            times.add(ldt);
            dbh.insertBadgeTime(ldt, OwnSettings.getTimePerDay() , daysCode);
        } else {
            Context context = App.getContext();
            CharSequence text = App.getContext().getText(R.string.errorNoNeedToWork);
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    public void removeWithValue(LocalDateTime localDateTime) {
        if (localDateTime!=null) {
            this.times.remove(localDateTime);
            dbh.deleteBadgeTimes(localDateTime);
        }

    }

    public void updateBadgeTime(LocalDateTime oldTime, LocalDateTime newTime) {
        dbh.updateBadgeTimes(oldTime, newTime);
        this.times.remove(oldTime);
        this.times.add(newTime);
    }

    public float getBadgedTime(CardType ct, LocalDateTime ldt) {
        float max = 0f;

        long daysBetween = 1;
        LocalDate startDate;
        switch (ct) {
            case DAY:
                startDate = ldt.toLocalDate();
                daysBetween = 1;
                max = getSecondsBetweenDays(daysBetween, startDate);
                break;
            case WEEK:
                startDate = ldt.with(DayOfWeek.MONDAY).toLocalDate();
                daysBetween = 7;
                max = getSecondsBetweenDays(daysBetween, startDate);
                break;
            case MONTH:
                startDate = ldt.withDayOfMonth(1).toLocalDate();
                daysBetween = startDate.lengthOfMonth();
                max = getSecondsBetweenDays(daysBetween, startDate);
                break;
        }
        return max;
    }


    public LocalDateTime[] lookForBadgedTime(LocalDateTime ldt, CardType ct) {
        LocalDateTime[] arr = null;
        ArrayList<LocalDateTime> local;
        switch (ct) {
            case DAY:
                arr = new LocalDateTime[1];
                local = getTimeStampsInDate(ldt.toLocalDate());
                if (local.size()>0) {
                    arr[0] = local.get(0);
                } else {
                    arr[0] = null;
                }
                break;
            case WEEK:
                arr = new LocalDateTime[7];
                int i = 0;
                for (DayOfWeek dow : DayOfWeek.values()) {
                    local = getTimeStampsInDate(ldt.with(dow).toLocalDate());
                    if (local.size()>0) {
                        arr[i] = local.get(0);
                    } else {
                        arr[i] = null;
                    }
                    i++;
                }
                break;
            case MONTH:

                LocalDate startDate = ldt.withDayOfMonth(1).toLocalDate();
                int daysBetween = startDate.lengthOfMonth();
                arr = new LocalDateTime[daysBetween];
                for (int j = 0;j<daysBetween;j++) {
                    local = getTimeStampsInDate(startDate.plusDays(j));
                    if (local.size()>0) {
                        arr[j] = local.get(0);
                    } else {
                        arr[j] = null;
                    }
                }
                break;
        }
        Boolean atleast1EleGood = false;
        for (LocalDateTime checkNull : arr) {
            if (checkNull!=null) {
                atleast1EleGood = true;
            }
        }
        if (atleast1EleGood) {
            return arr;
        } else {
            return null;
        }


    }
}
