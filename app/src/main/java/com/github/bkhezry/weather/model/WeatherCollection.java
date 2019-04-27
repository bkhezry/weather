package com.github.bkhezry.weather.model;

import com.github.bkhezry.weather.model.daysweather.ListItem;
import com.github.bkhezry.weather.model.fivedayweather.ListItemHourly;

import java.util.List;

public class WeatherCollection {
  private List<ListItemHourly> listItemHourlies;
  private ListItem listItem;

  public List<ListItemHourly> getListItemHourlies() {
    return listItemHourlies;
  }

  public void setListItemHourlies(List<ListItemHourly> listItemHourlies) {
    this.listItemHourlies = listItemHourlies;
  }

  public ListItem getListItem() {
    return listItem;
  }

  public void setListItem(ListItem listItem) {
    this.listItem = listItem;
  }
}
