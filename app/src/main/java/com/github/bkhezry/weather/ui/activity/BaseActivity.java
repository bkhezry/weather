package com.github.bkhezry.weather.ui.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.bkhezry.weather.utils.MyApplication;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class BaseActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void attachBaseContext(Context base) {
    Context newContext = MyApplication.localeManager.setLocale(base);
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newContext));
  }

  @Override
  public void applyOverrideConfiguration(@Nullable Configuration overrideConfiguration) {
    if (overrideConfiguration != null) {
      int uiMode = overrideConfiguration.uiMode;
      overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
      overrideConfiguration.uiMode = uiMode;
    }
    super.applyOverrideConfiguration(getResources().getConfiguration());
  }
}