package com.github.bkhezry.weather.model.fivedayweather;

import com.github.bkhezry.weather.model.common.Clouds;
import com.github.bkhezry.weather.model.common.WeatherItem;
import com.github.bkhezry.weather.model.common.Wind;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemHourly {

  @SerializedName("dt")
  private int dt;

  @SerializedName("dt_txt")
  private String dtTxt;

  @SerializedName("weather")
  private List<WeatherItem> weather;

  @SerializedName("main")
  private Main main;

  @SerializedName("clouds")
  private Clouds clouds;

  @SerializedName("sys")
  private Sys sys;

  @SerializedName("wind")
  private Wind wind;

  @SerializedName("rain")
  private Rain rain;

  public int getDt() {
    return dt;
  }

  public void setDt(int dt) {
    this.dt = dt;
  }

  public String getDtTxt() {
    return dtTxt;
  }

  public void setDtTxt(String dtTxt) {
    this.dtTxt = dtTxt;
  }

  public List<WeatherItem> getWeather() {
    return weather;
  }

  public void setWeather(List<WeatherItem> weather) {
    this.weather = weather;
  }

  public Main getMain() {
    return main;
  }

  public void setMain(Main main) {
    this.main = main;
  }

  public Clouds getClouds() {
    return clouds;
  }

  public void setClouds(Clouds clouds) {
    this.clouds = clouds;
  }

  public Sys getSys() {
    return sys;
  }

  public void setSys(Sys sys) {
    this.sys = sys;
  }

  public Wind getWind() {
    return wind;
  }

  public void setWind(Wind wind) {
    this.wind = wind;
  }

  public Rain getRain() {
    return rain;
  }

  public void setRain(Rain rain) {
    this.rain = rain;
  }
}