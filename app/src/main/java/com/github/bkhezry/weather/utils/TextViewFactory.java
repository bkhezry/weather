package com.github.bkhezry.weather.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.StyleRes;

public class TextViewFactory implements ViewSwitcher.ViewFactory {

  @StyleRes
  private final int styleId;
  private final boolean center;
  private final Context context;
  private final Typeface typeface;

  public TextViewFactory(Context context, @StyleRes int styleId, boolean center, Typeface typeface) {
    this.styleId = styleId;
    this.center = center;
    this.context = context;
    this.typeface = typeface;
  }

  @Override
  public View makeView() {
    final TextView textView = new TextView(context);
    if (center) {
      textView.setGravity(Gravity.CENTER);
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      textView.setTextAppearance(context, styleId);
    } else {
      textView.setTextAppearance(styleId);
    }
    textView.setTypeface(typeface);
    return textView;
  }

}
