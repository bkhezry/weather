package com.github.bkhezry.weather.model.daysweather;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MultipleDaysWeatherResponse {

  @SerializedName("city")
  private City city;

  @SerializedName("cnt")
  private int cnt;

  @SerializedName("cod")
  private String cod;

  @SerializedName("message")
  private double message;

  @SerializedName("list")
  private List<ListItem> list;

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public int getCnt() {
    return cnt;
  }

  public void setCnt(int cnt) {
    this.cnt = cnt;
  }

  public String getCod() {
    return cod;
  }

  public void setCod(String cod) {
    this.cod = cod;
  }

  public double getMessage() {
    return message;
  }

  public void setMessage(double message) {
    this.message = message;
  }

  public List<ListItem> getList() {
    return list;
  }

  public void setList(List<ListItem> list) {
    this.list = list;
  }
}