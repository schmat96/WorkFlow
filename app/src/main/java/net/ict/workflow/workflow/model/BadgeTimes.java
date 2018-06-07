package net.ict.workflow.workflow.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;

public class BadgeTimes {
    ArrayList<LocalDateTime> times;

    private static final String TAG = "MyActivity";
    private static final float MAX = 8.24f;
    private static final int WORK_DAYS = 5;

    public BadgeTimes() {
        times = new ArrayList<>();
    }

    public void init() {
        for (int i = 0; i<10;i++) {
            LocalDateTime ldt = LocalDateTime.now().plusHours(i+10);
            times.add(ldt);
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

    public float getBadgedTimeDay(LocalDateTime date) {
        LocalDate startDate = date.toLocalDate();
        long daysBetween = 1;
        float max = getSecondsBetweenDays(daysBetween, startDate);
        return max;
    }

    public float getBadgedTimeWeek(LocalDateTime date) {
        LocalDate startDate = date.with(DayOfWeek.MONDAY).toLocalDate();
        long daysBetween = 7;
        float max = getSecondsBetweenDays(daysBetween, startDate);
        return max;
    }

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
                return getDaysOfMonth(ldt);
            default:
                return MAX;
        }
    }

    private int getDaysOfMonth(LocalDateTime ldt){
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
}
