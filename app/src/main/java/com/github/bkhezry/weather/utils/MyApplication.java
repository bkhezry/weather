package com.github.bkhezry.weather.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.github.bkhezry.weather.BuildConfig;
import com.github.bkhezry.weather.model.db.MyObjectBox;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

public class MyApplication extends Application {
  private static BoxStore boxStore;
  public static LocaleManager localeManager;

  public static BoxStore getBoxStore() {
    return boxStore;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    createBoxStore();
  }

  private void createBoxStore() {
    boxStore = MyObjectBox.builder().androidContext(MyApplication.this).build();
    if (BuildConfig.DEBUG) {
      new AndroidObjectBrowser(boxStore).start(this);
    }
  }

  @Override
  protected void attachBaseContext(Context base) {
    localeManager = new LocaleManager(base);
    super.attachBaseContext(localeManager.setLocale(base));
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    localeManager.setLocale(this);
  }
}
