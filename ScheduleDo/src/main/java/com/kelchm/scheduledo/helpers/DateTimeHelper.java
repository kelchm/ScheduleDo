package com.kelchm.scheduledo.helpers;

import org.joda.time.DateTime;

/**
 * Created by kelchm on 11/24/13.
 */
public class DateTimeHelper {

    public static boolean isYesterday(DateTime date) {
        DateTime yesterday = DateTime.now().minusDays(1);
        boolean day = date.getDayOfMonth() == yesterday.getDayOfMonth();
        boolean month = date.getMonthOfYear() == yesterday.getMonthOfYear();
        boolean year = date.getYear() == yesterday.getYear();
        boolean result = (day && month && year);

        return result;
    }


    public static boolean isToday(DateTime date) {
        DateTime now = DateTime.now();
        boolean day = date.getDayOfMonth() == now.getDayOfMonth();
        boolean month = date.getMonthOfYear() == now.getMonthOfYear();
        boolean year = date.getYear() == now.getYear();
        boolean result = (day && month && year);

        return result;
    }

    public static boolean isTomorrow(DateTime date) {
        DateTime tomorrow = DateTime.now().plusDays(1);
        boolean day = date.getDayOfMonth() == tomorrow.getDayOfMonth();
        boolean month = date.getMonthOfYear() == tomorrow.getMonthOfYear();
        boolean year = date.getYear() == tomorrow.getYear();
        boolean result = (day && month && year);

        return result;
    }
}
