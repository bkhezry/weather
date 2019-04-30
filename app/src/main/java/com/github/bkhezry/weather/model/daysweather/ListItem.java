package com.github.bkhezry.weather.model.daysweather;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.model.common.WeatherItem;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.ButterKnife;

public class ListItem extends AbstractItem<ListItem, ListItem.MyViewHolder> {

  @SerializedName("dt")
  private int dt;

  @SerializedName("temp")
  private Temp temp;

  @SerializedName("deg")
  private int deg;

  @SerializedName("weather")
  private List<WeatherItem> weather;

  @SerializedName("humidity")
  private int humidity;

  @SerializedName("pressure")
  private double pressure;

  @SerializedName("clouds")
  private int clouds;

  @SerializedName("speed")
  private double speed;

  @SerializedName("rain")
  private double rain;

  public int getDt() {
    return dt;
  }

  public void setDt(int dt) {
    this.dt = dt;
  }

  public Temp getTemp() {
    return temp;
  }

  public void setTemp(Temp temp) {
    this.temp = temp;
  }

  public int getDeg() {
    return deg;
  }

  public void setDeg(int deg) {
    this.deg = deg;
  }

  public List<WeatherItem> getWeather() {
    return weather;
  }

  public void setWeather(List<WeatherItem> weather) {
    this.weather = weather;
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

  public int getClouds() {
    return clouds;
  }

  public void setClouds(int clouds) {
    this.clouds = clouds;
  }

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public double getRain() {
    return rain;
  }

  public void setRain(double rain) {
    this.rain = rain;
  }

  @NonNull
  @Override
  public MyViewHolder getViewHolder(@NonNull View v) {
    return new MyViewHolder(v);
  }

  @Override
  public int getType() {
    return R.id.fastadapter_item_adapter;
  }

  @Override
  public int getLayoutRes() {
    return R.layout.multiple_days_item;
  }

  protected static class MyViewHolder extends FastAdapter.ViewHolder<ListItem> {
    Context context;
    View view;

    MyViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      this.view = view;
      this.context = view.getContext();
    }

    @Override
    public void bindView(@NonNull ListItem item, @NonNull List<Object> payloads) {

    }

    @Override
    public void unbindView(@NonNull ListItem item) {

    }

  }
}