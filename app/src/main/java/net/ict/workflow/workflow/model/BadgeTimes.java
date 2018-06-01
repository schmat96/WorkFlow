package net.ict.workflow.workflow.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;

public class BadgeTimes {
    ArrayList<LocalDateTime> times;

    private static final String TAG = "MyActivity";

    public BadgeTimes() {
        times = new ArrayList<>();
    }

    public void init() {
        for (int i = 0; i<20;i++) {
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
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(1);
        endDate = endDate.withDayOfMonth(endDate.lengthOfMonth());

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        float max = getSecondsBetweenDays(daysBetween, startDate);
        return max;
    }

    public float getBadgedTimeWeek(LocalDateTime date) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusWeeks(1);
        endDate = endDate.withDayOfMonth(endDate.lengthOfMonth());
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        float max = getSecondsBetweenDays(daysBetween, startDate);
        return max;
    }

    public float getBadgedTimeMonth(LocalDateTime date) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(1);
        endDate = endDate.withDayOfMonth(endDate.lengthOfMonth());

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        float max = getSecondsBetweenDays(daysBetween, startDate);
        return max;
    }

    public float getSecondsBetweenDays(long daysBetween, LocalDate startDate) {
        long times = 0;
        for(int i = 0; i <= daysBetween; i++){
            startDate.plusDays(i);
            ArrayList<LocalDateTime> dates = getTimeStampsInDate(startDate);
            LocalDateTime current = null;
            Boolean pauseOrWork = true;
            for (LocalDateTime ldt : dates) {
                if (current==null) {
                    current = ldt;
                } else {
                    if (pauseOrWork) {
                        times = times + (ChronoUnit.SECONDS.between(current, ldt));
                    } else {

                    }
                    current = ldt;
                }
            }
        }
        return times/(60*60*60);
    }

    public void addBadgeTime(LocalDateTime ldt) {
        times.add(ldt);
    }
}
