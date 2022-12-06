package site.controller;

import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;

public class DateUtils {

    public static String dateToString(DateTime dateTime) {
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
        return String.format(Locale.ENGLISH, format, new Date(dateTime.getMillis()), suffix);
    }

    public static String dateToStringWithMonth(DateTime dateTime) {
        return String.format(Locale.ENGLISH, "%1$s of %2$tB", dateToString(dateTime), new Date(dateTime.getMillis()));
    }

    public static String dateToStringWithMonthAndYear(DateTime dateTime) {
        return String.format(Locale.ENGLISH, "%1$s of %2$tY", dateToStringWithMonth(dateTime), new Date(dateTime.getMillis()));
    }
}
