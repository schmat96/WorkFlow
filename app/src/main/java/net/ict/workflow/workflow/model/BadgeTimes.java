package net.ict.workflow.workflow.model;

import com.google.common.collect.TreeRangeSet;

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
    private static final float MAX = 8.5f;
    private static final int WORK_DAYS = 5;

    public BadgeTimes() {
        times = new TreeSet<>();
    }

    public void init() {
        if (times.size()<=0) {
            for (int i = 0; i<800;i++) {
                LocalDateTime ldt = LocalDateTime.now().plusHours(i*3);
                ldt = ldt.plusMinutes(i*i/100);
                ldt = ldt.minusNanos(ldt.getNano());
                ldt = ldt.minusSeconds(ldt.getSecond());
                times.add(ldt);
            }
        }

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
        return times/(60*60);
    }

    public float getMax(CardType type, LocalDateTime ldt) {
        switch(type){
            case DAY:
                return MAX;
            case WEEK:
                return MAX * WORK_DAYS;
            case MONTH:
                return MAX * getDaysOfMonth(ldt);
            default:
                return MAX;
        }
    }

    private int getDaysOfMonth(LocalDateTime ldt){
        //TODO implement something like this:
        // although realistically this method should return
        // the number of workdays per month -> highly variable
        // implement some calendar function of some sorts
        /*
        date = ldt;
        int day = date.getDay();
        int remainingDays = 31 - day;
        localdatetime refDate = 2000;
        while (refDate.getMonth() != date.getMonth() && year.something){
            refDate = date.addDays(remainingDays);
            remainingDays--;
        }
         */
        switch(ldt.getMonth()){
            case JANUARY:
            case MARCH:
            case MAY:
            case JULY:
            case AUGUST:
            case OCTOBER:
            case DECEMBER:
                return 31;
            case APRIL:
            case JUNE:
            case SEPTEMBER:
            case NOVEMBER:
                return 30;
            case FEBRUARY:
                return 28;
                default:
                    return 31;
        }
    }


    public void addBadgeTime(LocalDateTime ldt) {
        times.add(ldt);
    }

    public void removeWithValue(LocalDateTime localDateTime) {
        this.times.remove(localDateTime);
    }

    public void updateBadgeTime(LocalDateTime oldTime, LocalDateTime newTime) {
        removeWithValue(oldTime);
        addBadgeTime(newTime);

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



}
