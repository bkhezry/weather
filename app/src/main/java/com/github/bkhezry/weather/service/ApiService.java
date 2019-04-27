package com.github.bkhezry.weather.service;

import com.github.bkhezry.weather.model.currentweather.CurrentWeatherResponse;

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
}
