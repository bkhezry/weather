package com.github.bkhezry.weather.model.fivedayweather;

import com.google.gson.annotations.SerializedName;

public class Rain {

  @SerializedName("3h")
  private double jsonMember3h;

  public double getJsonMember3h() {
    return jsonMember3h;
  }

  public void setJsonMember3h(double jsonMember3h) {
    this.jsonMember3h = jsonMember3h;
  }
}