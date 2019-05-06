package com.github.bkhezry.weather.utils;

import com.github.bkhezry.weather.model.db.CurrentWeather;

import io.objectbox.Box;
import io.objectbox.query.Query;

public class DbUtil {

  public static Query<CurrentWeather> getCurrentWeatherQuery(Box<CurrentWeather> currentWeatherBox) {
    return currentWeatherBox.query().build();
  }
}
