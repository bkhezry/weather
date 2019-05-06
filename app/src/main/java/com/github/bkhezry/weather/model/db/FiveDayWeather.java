package com.github.bkhezry.weather.model.db;

import androidx.annotation.ColorInt;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class FiveDayWeather {
  @Id
  private long id;
  private int dt;
  private double temp;
  private double minTemp;
  private double maxTemp;
  private int weatherId;
  private long timestampStart;
  private long timestampEnd;
  private @ColorInt
  int color;
  private @ColorInt
  int colorAlpha;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getDt() {
    return dt;
  }

  public void setDt(int dt) {
    this.dt = dt;
  }

  public double getTemp() {
    return temp;
  }

  public void setTemp(double temp) {
    this.temp = temp;
  }

  public double getMinTemp() {
    return minTemp;
  }

  public void setMinTemp(double minTemp) {
    this.minTemp = minTemp;
  }

  public double getMaxTemp() {
    return maxTemp;
  }

  public void setMaxTemp(double maxTemp) {
    this.maxTemp = maxTemp;
  }

  public int getWeatherId() {
    return weatherId;
  }

  public void setWeatherId(int weatherId) {
    this.weatherId = weatherId;
  }

  public long getTimestampStart() {
    return timestampStart;
  }

  public void setTimestampStart(long timestampStart) {
    this.timestampStart = timestampStart;
  }

  public long getTimestampEnd() {
    return timestampEnd;
  }

  public void setTimestampEnd(long timestampEnd) {
    this.timestampEnd = timestampEnd;
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
  }

  public int getColorAlpha() {
    return colorAlpha;
  }

  public void setColorAlpha(int colorAlpha) {
    this.colorAlpha = colorAlpha;
  }
}
