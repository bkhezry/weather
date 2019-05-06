package com.github.bkhezry.weather.model.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class ItemHourly {
  @Id
  private long id;
  private long fiveDayWeatherId;
  private int dt;
  private double temp;
  private int weatherCode;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getFiveDayWeatherId() {
    return fiveDayWeatherId;
  }

  public void setFiveDayWeatherId(long fiveDayWeatherId) {
    this.fiveDayWeatherId = fiveDayWeatherId;
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

  public int getWeatherCode() {
    return weatherCode;
  }

  public void setWeatherCode(int weatherCode) {
    this.weatherCode = weatherCode;
  }
}
