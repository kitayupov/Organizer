package com.vudn.kit.organizer.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {

    public static String getDateString(long date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
        return dateFormat.format(new Date(date));
    }

    public static String getTimeString(long date) {
        final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.ROOT);
        return timeFormat.format(new Date(date));
    }

    public static String getDateTimeString(long date) {
        final SimpleDateFormat timeFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.ROOT);
        return timeFormat.format(new Date(date));
    }
}
