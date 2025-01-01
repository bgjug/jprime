package site.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String dateToString(LocalDateTime dateTime) {
        int day = dateTime.getDayOfMonth();
        String suffix = "th";
        if (day < 11 || day > 13) {
            suffix = switch (day % 10) {
                case 1 -> "st";
                case 2 -> "nd";
                case 3 -> "rd";
                default -> "th";
            };
        }

        String format = "%1$te%2$s";
        return String.format(Locale.ENGLISH, format, fromLocalDateTime(dateTime), suffix);
    }

    public static Date fromLocalDateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(ZoneOffset.from(ZonedDateTime.now())));
    }

    public static String dateToStringWithMonth(LocalDateTime dateTime) {
        return String.format(Locale.ENGLISH, "%1$s of %2$tB", dateToString(dateTime), fromLocalDateTime(dateTime));
    }

    public static String dateToStringWithMonthAndYear(LocalDateTime dateTime) {
        return String.format(Locale.ENGLISH, "%1$s of %2$tY", dateToStringWithMonth(dateTime), fromLocalDateTime(dateTime));
    }

    public static String formatDuration(Duration duration) {
        long days = duration.toDays();
        if (days > 0) {
            return String.format("%d days", days);
        }

        if (duration.toHours() > 0) {
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            long seconds = duration.getSeconds() % 60;

            // Format as "HH:mm:ss"
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        long minutes = duration.toMinutes();
        if (minutes > 0) {
            long seconds = duration.getSeconds() % 60;

            // Format as "mm:ss"
            return String.format("%02d:%02d", minutes, seconds);
        }

        return String.format("%d seconds", duration.getSeconds());
    }

    public static LocalDateTime toLocalDateTime(Date startDate) {
        return startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
