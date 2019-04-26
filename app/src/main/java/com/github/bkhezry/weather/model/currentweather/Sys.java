package com.github.bkhezry.weather.model.currentweather;

import com.google.gson.annotations.SerializedName;

public class Sys {

  @SerializedName("country")
  private String country;

  @SerializedName("sunrise")
  private int sunrise;

  @SerializedName("sunset")
  private int sunset;

  @SerializedName("message")
  private double message;

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public int getSunrise() {
    return sunrise;
  }

  public void setSunrise(int sunrise) {
    this.sunrise = sunrise;
  }

  public int getSunset() {
    return sunset;
  }

  public void setSunset(int sunset) {
    this.sunset = sunset;
  }

  public double getMessage() {
    return message;
  }

  public void setMessage(double message) {
    this.message = message;
  }
}