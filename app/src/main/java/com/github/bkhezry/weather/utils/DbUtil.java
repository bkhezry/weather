package com.github.bkhezry.weather.utils;

import com.github.bkhezry.weather.model.db.CurrentWeather;
import com.github.bkhezry.weather.model.db.FiveDayWeather;
import com.github.bkhezry.weather.model.db.ItemHourlyDB;
import com.github.bkhezry.weather.model.db.ItemHourlyDB_;
import com.github.bkhezry.weather.model.db.MultipleDaysWeather;

import io.objectbox.Box;
import io.objectbox.query.Query;

public class DbUtil {


  /**
   * Get query of currentWeatherBox
   *
   * @param currentWeatherBox instance of {@link Box<CurrentWeather>}
   * @return instance of {@link Query<CurrentWeather>}
   */
  public static Query<CurrentWeather> getCurrentWeatherQuery(Box<CurrentWeather> currentWeatherBox) {
    return currentWeatherBox.query().build();
  }

  /**
   * Get query of fiveDayWeatherBox
   *
   * @param fiveDayWeatherBox instance of {@link Box<FiveDayWeather>}
   * @return instance of {@link Query<FiveDayWeather>}
   */
  public static Query<FiveDayWeather> getFiveDayWeatherQuery(Box<FiveDayWeather> fiveDayWeatherBox) {
    return fiveDayWeatherBox.query().build();
  }

  /**
   * Get query of itemHourlyDBBox according to fiveDayWeatherId value
   *
   * @param itemHourlyDBBox  instance of {@link Box<ItemHourlyDB>}
   * @param fiveDayWeatherId int key of five day weather id
   * @return instance of {@link Query<ItemHourlyDB>}
   */
  public static Query<ItemHourlyDB> getItemHourlyDBQuery(Box<ItemHourlyDB> itemHourlyDBBox, long fiveDayWeatherId) {
    return itemHourlyDBBox.query()
        .equal(ItemHourlyDB_.fiveDayWeatherId, fiveDayWeatherId)
        .build();
  }

  /**
   * Get query of multipleDaysWeatherBox
   *
   * @param multipleDaysWeatherBox instance of {@link Box<MultipleDaysWeather>}
   * @return instance of {@link Query<MultipleDaysWeather>}
   */
  public static Query<MultipleDaysWeather> getMultipleDaysWeatherQuery(Box<MultipleDaysWeather> multipleDaysWeatherBox) {
    return multipleDaysWeatherBox.query().build();
  }
}
