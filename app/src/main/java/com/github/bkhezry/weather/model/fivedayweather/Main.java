package com.github.bkhezry.weather.model.fivedayweather;

import com.google.gson.annotations.SerializedName;

public class Main {

  @SerializedName("temp")
  private double temp;

  @SerializedName("temp_min")
  private double tempMin;

  @SerializedName("grnd_level")
  private double grndLevel;

  @SerializedName("temp_kf")
  private double tempKf;

  @SerializedName("humidity")
  private int humidity;

  @SerializedName("pressure")
  private double pressure;

  @SerializedName("sea_level")
  private double seaLevel;

  @SerializedName("temp_max")
  private double tempMax;

  public double getTemp() {
    return temp;
  }

  public void setTemp(double temp) {
    this.temp = temp;
  }

  public double getTempMin() {
    return tempMin;
  }

  public void setTempMin(double tempMin) {
    this.tempMin = tempMin;
  }

  public double getGrndLevel() {
    return grndLevel;
  }

  public void setGrndLevel(double grndLevel) {
    this.grndLevel = grndLevel;
  }

  public double getTempKf() {
    return tempKf;
  }

  public void setTempKf(double tempKf) {
    this.tempKf = tempKf;
  }

  public int getHumidity() {
    return humidity;
  }

  public void setHumidity(int humidity) {
    this.humidity = humidity;
  }

  public double getPressure() {
    return pressure;
  }

  public void setPressure(double pressure) {
    this.pressure = pressure;
  }

  public double getSeaLevel() {
    return seaLevel;
  }

  public void setSeaLevel(double seaLevel) {
    this.seaLevel = seaLevel;
  }

  public double getTempMax() {
    return tempMax;
  }

  public void setTempMax(double tempMax) {
    this.tempMax = tempMax;
  }
}