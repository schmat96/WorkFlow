package net.ict.workflow.workflow;

        import java.time.LocalDateTime;

public class Converter {

    public static String convertLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.getDayOfWeek()+":"+localDateTime.getMonth()+":"+localDateTime.getYear()+" - "+localDateTime.getHour()+":"+localDateTime.getMinute();
    }

    public static Boolean[] getWeekDays() {
        Boolean[] weekDays = new Boolean[7];
        int setting = 113;
        int i = 0;
        while (i<=6) {
            if (setting % 2 == 0) {
                weekDays[i] = false;
            } else {
                weekDays[i] = true;
                setting--;
            }
            setting = setting / 2;
            i++;
        }
        return weekDays;
    }
}
