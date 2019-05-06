package com.github.bkhezry.weather.model.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class FiveDayWeaher {
  @Id
  private long id;
  private int dt;
  private double temp;
  private double minTemp;
  private double maxTemp;
  private int weatherId;
}
