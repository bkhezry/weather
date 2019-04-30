package com.github.bkhezry.weather.utils;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.github.bkhezry.weather.R;

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

  public static void setWeatherIcon(Context context, AppCompatImageView imageView, int weatherCode) {
    if (weatherCode / 100 == 2) {
      Glide.with(context).load(R.drawable.thunderstorm_day).into(imageView);
    } else if (weatherCode / 100 == 3) {
      Glide.with(context).load(R.drawable.rainy_weather).into(imageView);
    } else if (weatherCode / 100 == 5) {
      Glide.with(context).load(R.drawable.rainy_day).into(imageView);
    } else if (weatherCode / 100 == 6) {
      Glide.with(context).load(R.drawable.snow_weather).into(imageView);
    } else if (weatherCode / 100 == 7) {
      Glide.with(context).load(R.drawable.unknown).into(imageView);
    } else if (weatherCode == 800) {
      Glide.with(context).load(R.drawable.clear_day).into(imageView);
    } else if (weatherCode == 801) {
      Glide.with(context).load(R.drawable.few_clouds_day).into(imageView);
    } else if (weatherCode == 803) {
      Glide.with(context).load(R.drawable.broken_clouds).into(imageView);
    } else if (weatherCode / 100 == 8) {
      Glide.with(context).load(R.drawable.cloudy_weather).into(imageView);
    }
  }

  public static void showFragment(Fragment fragment, FragmentManager fragmentManager, boolean withAnimation) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    if (withAnimation) {
      transaction.setCustomAnimations(R.anim.slide_up_anim, R.anim.slide_down_anim);
    } else {
      transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
    }
    transaction.add(android.R.id.content, fragment).addToBackStack(null).commit();
  }

  public static String getTime(Calendar calendar) {
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    String hourString;
    if (hour < 10) {
      hourString = "0" + hour;
    } else {
      hourString = String.valueOf(hour);
    }
    String minuteString;
    if (minute < 10) {
      minuteString = "0" + minute;
    } else {
      minuteString = String.valueOf(minute);
    }
    return hourString + ":" + minuteString;
  }
}
