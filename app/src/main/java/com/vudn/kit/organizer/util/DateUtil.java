package com.vudn.kit.organizer.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String getDateString(long date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
        return dateFormat.format(new Date(date));
    }
}
