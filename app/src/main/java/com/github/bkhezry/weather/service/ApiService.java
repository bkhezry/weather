package com.github.bkhezry.weather.service;

import com.github.bkhezry.weather.model.currentweather.CurrentWeatherResponse;
import com.github.bkhezry.weather.model.daysweather.MultipleDaysWeatherResponse;
import com.github.bkhezry.weather.model.fivedayweather.FiveDayResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

  // Get current weather
  @GET("weather")
  Single<CurrentWeatherResponse> getCurrentWeather(
      @Query("q") String q,
      @Query("units") String units,
      @Query("lang") String lang,
      @Query("appid") String appId
  );

  // Get five days weather
  @GET("forecast")
  Single<FiveDayResponse> getFiveDaysWeather(
      @Query("q") String q,
      @Query("units") String units,
      @Query("lang") String lang,
      @Query("appid") String appId
  );

  // Get multiple days weather
  @GET("forecast/daily")
  Single<MultipleDaysWeatherResponse> getMultipleDaysWeather(
      @Query("q") String q,
      @Query("units") String units,
      @Query("lang") String lang,
      @Query("cnt") int dayCount,
      @Query("appid") String appId
  );
}
