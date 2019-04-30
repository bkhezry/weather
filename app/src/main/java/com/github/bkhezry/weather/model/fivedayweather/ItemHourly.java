package com.github.bkhezry.weather.model.fivedayweather;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.model.common.Clouds;
import com.github.bkhezry.weather.model.common.WeatherItem;
import com.github.bkhezry.weather.model.common.Wind;
import com.github.bkhezry.weather.utils.AppUtil;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemHourly extends AbstractItem<ItemHourly, ItemHourly.MyViewHolder> {

  @SerializedName("dt")
  private int dt;

  @SerializedName("dt_txt")
  private String dtTxt;

  @SerializedName("weather")
  private List<WeatherItem> weather;

  @SerializedName("main")
  private Main main;

  @SerializedName("clouds")
  private Clouds clouds;

  @SerializedName("sys")
  private Sys sys;

  @SerializedName("wind")
  private Wind wind;

  @SerializedName("rain")
  private Rain rain;

  public int getDt() {
    return dt;
  }

  public void setDt(int dt) {
    this.dt = dt;
  }

  public String getDtTxt() {
    return dtTxt;
  }

  public void setDtTxt(String dtTxt) {
    this.dtTxt = dtTxt;
  }

  public List<WeatherItem> getWeather() {
    return weather;
  }

  public void setWeather(List<WeatherItem> weather) {
    this.weather = weather;
  }

  public Main getMain() {
    return main;
  }

  public void setMain(Main main) {
    this.main = main;
  }

  public Clouds getClouds() {
    return clouds;
  }

  public void setClouds(Clouds clouds) {
    this.clouds = clouds;
  }

  public Sys getSys() {
    return sys;
  }

  public void setSys(Sys sys) {
    this.sys = sys;
  }

  public Wind getWind() {
    return wind;
  }

  public void setWind(Wind wind) {
    this.wind = wind;
  }

  public Rain getRain() {
    return rain;
  }

  public void setRain(Rain rain) {
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
    return R.layout.weather_hourly_item;
  }

  protected static class MyViewHolder extends FastAdapter.ViewHolder<ItemHourly> {
    View view;
    Context context;
    @BindView(R.id.time_text_view)
    AppCompatTextView timeTextView;
    @BindView(R.id.weather_image_view)
    AppCompatImageView weatherImageView;
    @BindView(R.id.temp_text_view)
    AppCompatTextView tempTextView;

    MyViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      this.view = view;
      this.context = view.getContext();
    }

    @Override
    public void bindView(@NonNull ItemHourly item, @NonNull List<Object> payloads) {
      Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
      calendar.setTimeInMillis(item.getDt() * 1000L);
      timeTextView.setText(AppUtil.getTime(calendar));
      tempTextView.setText(String.format(Locale.getDefault(), "%.0f°", item.getMain().getTemp()));
      int weatherCode = item.getWeather().get(0).getId();
      AppUtil.setWeatherIcon(context, weatherImageView, weatherCode);
    }

    @Override
    public void unbindView(@NonNull ItemHourly item) {

    }

  }
}