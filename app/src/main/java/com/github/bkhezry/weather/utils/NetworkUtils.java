package com.github.bkhezry.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.RequiresPermission;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

public final class NetworkUtils {

  private NetworkUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }


  /**
   * Return whether network is connected.
   * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}</p>
   *
   * @return {@code true}: connected<br>{@code false}: disconnected
   */
  @RequiresPermission(ACCESS_NETWORK_STATE)
  public static boolean isConnected() {
    NetworkInfo info = getActiveNetworkInfo();
    return info != null && info.isConnected();
  }


  @RequiresPermission(ACCESS_NETWORK_STATE)
  private static NetworkInfo getActiveNetworkInfo() {
    ConnectivityManager cm =
        (ConnectivityManager) AppUtil.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
    if (cm == null) return null;
    return cm.getActiveNetworkInfo();
  }
}

