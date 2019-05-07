package com.github.bkhezry.weather.model.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class MultipleDaysWeather {
  @Id
  private long id;
  private int dt;
  private int weatherId;
  private double minTemp;
  private double maxTemp;

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

  public int getWeatherId() {
    return weatherId;
  }

  public void setWeatherId(int weatherId) {
    this.weatherId = weatherId;
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
}
