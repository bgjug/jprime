package site.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String dateToString(LocalDateTime dateTime) {
        int day = dateTime.getDayOfMonth();
        String suffix = "th";
        if (day < 11 || day > 13) {
            switch (day % 10) {
                case 1:
                    suffix = "st";
                    break;
                case 2:
                    suffix = "nd";
                    break;
                case 3:
                    suffix = "rd";
                    break;
                default:
                    suffix = "th";
            }
        }

        String format = "%1$te%2$s";
        return String.format(Locale.ENGLISH, format, fromLocalDateTime(dateTime), suffix);
    }

    public static Date fromLocalDateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String dateToStringWithMonth(LocalDateTime dateTime) {
        return String.format(Locale.ENGLISH, "%1$s of %2$tB", dateToString(dateTime), fromLocalDateTime(dateTime));
    }

    public static String dateToStringWithMonthAndYear(LocalDateTime dateTime) {
        return String.format(Locale.ENGLISH, "%1$s of %2$tY", dateToStringWithMonth(dateTime), fromLocalDateTime(dateTime));
    }
}
