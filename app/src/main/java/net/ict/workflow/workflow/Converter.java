package net.ict.workflow.workflow;

        import android.util.Log;

        import java.time.LocalDateTime;
        import java.time.format.DateTimeFormatter;
        import net.ict.workflow.workflow.model.DateFormats;
        import net.ict.workflow.workflow.model.OwnSettings;

public class Converter {

    public static String convertLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.getDayOfWeek()+":"+localDateTime.getMonth()+":"+localDateTime.getYear()+" - "+localDateTime.getHour()+":"+localDateTime.getMinute();
    }

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormats.DB_FORMAT);
        return localDateTime.format(formatter);
    }

    public static LocalDateTime stringToLocalDateTime(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormats.DB_FORMAT);
        Log.e("DB_Parse_String", stringDate);
        return LocalDateTime.parse(stringDate, formatter);
    }

    public static Boolean[] getWeekDays() {
        Boolean[] weekDays = OwnSettings.getWeeks();
        return weekDays;
    }

    public static int getIntWeekDays(Boolean[] weeks) {
        int id = 0;
        for (int i=0;i<weeks.length;i++) {
            if (weeks[i]) {
                id = id + (int) (Math.pow(2, i));
            }
        }
        return id;
    }
}
