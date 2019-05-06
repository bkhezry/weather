package com.github.bkhezry.weather.utils;

import com.github.bkhezry.weather.model.db.CurrentWeather;
import com.github.bkhezry.weather.model.db.FiveDayWeather;

import io.objectbox.Box;
import io.objectbox.query.Query;

public class DbUtil {

  public static Query<CurrentWeather> getCurrentWeatherQuery(Box<CurrentWeather> currentWeatherBox) {
    return currentWeatherBox.query().build();
  }

  public static Query<FiveDayWeather> getFiveDayWeatherQuery(Box<FiveDayWeather> fiveDayWeatherBox) {
    return fiveDayWeatherBox.query().build();
  }
}
