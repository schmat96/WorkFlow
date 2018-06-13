package net.ict.workflow.workflow;

        import android.util.Log;

        import java.time.LocalDateTime;
        import java.time.format.DateTimeFormatter;
        import net.ict.workflow.workflow.model.DateFormats;

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
