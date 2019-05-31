package com.github.bkhezry.weather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {
  private static SharedPreferencesUtil instance = null;
  private SharedPreferences prefs;

  private SharedPreferencesUtil(Context context) {
    prefs = PreferenceManager.getDefaultSharedPreferences(context);
  }

  public static SharedPreferencesUtil getInstance(Context context) {

    if (instance == null) {
      instance = new SharedPreferencesUtil(context);
    }

    return instance;
  }

  public boolean isDarkThemeEnabled() {
    return prefs.getBoolean(Constants.DARK_THEME, false);
  }

  public void setDarkThemeEnabled(boolean state) {
    prefs.edit().putBoolean(Constants.DARK_THEME, state).apply();
  }
}
