package com.github.bkhezry.weather.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Locale;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.N;

public class LocaleManager {
  public static final String LANGUAGE_ENGLISH = "en";
  public static final String LANGUAGE_PERSIAN = "fa";
  private final SharedPreferences prefs;

  LocaleManager(Context context) {
    prefs = PreferenceManager.getDefaultSharedPreferences(context);
  }

  public static Locale getLocale(Resources res) {
    Configuration config = res.getConfiguration();
    return AppUtil.isAtLeastVersion(N) ? config.getLocales().get(0) : config.locale;
  }

  public Context setLocale(Context c) {
    return updateResources(c, getLanguage());
  }

  public Context setNewLocale(Context c, String language) {
    persistLanguage(language);
    return updateResources(c, language);
  }

  public String getLanguage() {
    return prefs.getString(Constants.LANGUAGE, LANGUAGE_ENGLISH);
  }

  @SuppressLint("ApplySharedPref")
  private void persistLanguage(String language) {
    prefs.edit().putString(Constants.LANGUAGE, language).commit();
  }

  private Context updateResources(Context context, String language) {
    Locale locale = new Locale(language);
    Locale.setDefault(locale);

    Resources res = context.getResources();
    Configuration config = new Configuration(res.getConfiguration());
    if (AppUtil.isAtLeastVersion(JELLY_BEAN_MR1)) {
      config.setLocale(locale);
      context = context.createConfigurationContext(config);
    } else {
      config.locale = locale;
      res.updateConfiguration(config, res.getDisplayMetrics());
    }
    return context;
  }

}

