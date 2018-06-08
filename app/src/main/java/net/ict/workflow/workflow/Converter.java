package net.ict.workflow.workflow;

        import java.time.LocalDateTime;

class Converter {

    public static String convertLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.getDayOfWeek()+":"+localDateTime.getMonth()+":"+localDateTime.getYear()+" - "+localDateTime.getHour()+":"+localDateTime.getMinute();
    }
}
