package com.github.bkhezry.weather.model.common;

import com.google.gson.annotations.SerializedName;

public class Coord {

  @SerializedName("lon")
  private double lon;

  @SerializedName("lat")
  private double lat;

  public double getLon() {
    return lon;
  }

  public void setLon(double lon) {
    this.lon = lon;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }
}