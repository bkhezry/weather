package com.github.bkhezry.weather.utils;

import com.github.bkhezry.weather.model.db.CurrentWeather;
import com.github.bkhezry.weather.model.db.FiveDayWeather;
import com.github.bkhezry.weather.model.db.ItemHourlyDB;
import com.github.bkhezry.weather.model.db.ItemHourlyDB_;
import com.github.bkhezry.weather.model.db.MultipleDaysWeather;

import io.objectbox.Box;
import io.objectbox.query.Query;

public class DbUtil {

  public static Query<CurrentWeather> getCurrentWeatherQuery(Box<CurrentWeather> currentWeatherBox) {
    return currentWeatherBox.query().build();
  }

  public static Query<FiveDayWeather> getFiveDayWeatherQuery(Box<FiveDayWeather> fiveDayWeatherBox) {
    return fiveDayWeatherBox.query().build();
  }

  public static Query<ItemHourlyDB> getItemHourlyDBQuery(Box<ItemHourlyDB> itemHourlyDBBox, long fiveDayWeatherId) {
    return itemHourlyDBBox.query()
        .equal(ItemHourlyDB_.fiveDayWeatherId, fiveDayWeatherId)
        .build();
  }

  public static Query<MultipleDaysWeather> getMultipleDaysWeatherQuery(Box<MultipleDaysWeather> multipleDaysWeatherBox) {
    return multipleDaysWeatherBox.query().build();
  }
}
