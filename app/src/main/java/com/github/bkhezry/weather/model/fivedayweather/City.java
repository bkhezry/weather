package com.github.bkhezry.weather.model.fivedayweather;

import com.github.bkhezry.weather.model.common.Coord;
import com.google.gson.annotations.SerializedName;

public class City {

  @SerializedName("country")
  private String country;

  @SerializedName("coord")
  private Coord coord;

  @SerializedName("name")
  private String name;

  @SerializedName("id")
  private int id;

  @SerializedName("population")
  private int population;

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Coord getCoord() {
    return coord;
  }

  public void setCoord(Coord coord) {
    this.coord = coord;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getPopulation() {
    return population;
  }

  public void setPopulation(int population) {
    this.population = population;
  }
}