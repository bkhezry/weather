package com.github.bkhezry.weather.model.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class CurrentWeather {
  @Id
  private long id;
  private double temp;
  private int humidity;
  private String description;
  private String main;
  private int weatherId;
  private double windDeg;
  private double windSpeed;
  private long storeTimestamp;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public double getTemp() {
    return temp;
  }

  public void setTemp(double temp) {
    this.temp = temp;
  }

  public int getHumidity() {
    return humidity;
  }

  public void setHumidity(int humidity) {
    this.humidity = humidity;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getMain() {
    return main;
  }

  public void setMain(String main) {
    this.main = main;
  }

  public int getWeatherId() {
    return weatherId;
  }

  public void setWeatherId(int weatherId) {
    this.weatherId = weatherId;
  }

  public double getWindDeg() {
    return windDeg;
  }

  public void setWindDeg(double windDeg) {
    this.windDeg = windDeg;
  }

  public double getWindSpeed() {
    return windSpeed;
  }

  public void setWindSpeed(double windSpeed) {
    this.windSpeed = windSpeed;
  }

  public long getStoreTimestamp() {
    return storeTimestamp;
  }

  public void setStoreTimestamp(long storeTimestamp) {
    this.storeTimestamp = storeTimestamp;
  }
}
