package com.github.bkhezry.weather.utils;

import java.util.Calendar;
import java.util.TimeZone;

public class AppUtil {

  public static long getStartOfDayTimestamp(Calendar calendar) {
    Calendar newCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"));
    newCalendar.setTimeInMillis(calendar.getTimeInMillis());
    newCalendar.set(Calendar.HOUR_OF_DAY, 0);
    newCalendar.set(Calendar.MINUTE, 0);
    newCalendar.set(Calendar.SECOND, 0);
    newCalendar.set(Calendar.MILLISECOND, 0);
    return newCalendar.getTimeInMillis();
  }

  public static long getEndOfDayTimestamp(Calendar calendar) {
    Calendar newCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"));
    newCalendar.setTimeInMillis(calendar.getTimeInMillis());
    newCalendar.set(Calendar.HOUR_OF_DAY, 23);
    newCalendar.set(Calendar.MINUTE, 59);
    newCalendar.set(Calendar.SECOND, 59);
    newCalendar.set(Calendar.MILLISECOND, 0);
    return newCalendar.getTimeInMillis();
  }

  public static Calendar addDays(Calendar cal, int days) {
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"));
    calendar.setTimeInMillis(cal.getTimeInMillis());
    calendar.add(Calendar.DATE, days);
    return calendar;
  }
}
