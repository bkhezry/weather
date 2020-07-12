package com.github.bkhezry.weather.model.db;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.databinding.WeatherHourlyItemBinding;
import com.github.bkhezry.weather.utils.AppUtil;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class ItemHourlyDB extends AbstractItem<ItemHourlyDB, ItemHourlyDB.MyViewHolder> {
  @Id
  private long id;
  private long fiveDayWeatherId;
  private int dt;
  private double temp;
  private int weatherCode;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getFiveDayWeatherId() {
    return fiveDayWeatherId;
  }

  public void setFiveDayWeatherId(long fiveDayWeatherId) {
    this.fiveDayWeatherId = fiveDayWeatherId;
  }

  public int getDt() {
    return dt;
  }

  public void setDt(int dt) {
    this.dt = dt;
  }

  public double getTemp() {
    return temp;
  }

  public void setTemp(double temp) {
    this.temp = temp;
  }

  public int getWeatherCode() {
    return weatherCode;
  }

  public void setWeatherCode(int weatherCode) {
    this.weatherCode = weatherCode;
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

  protected static class MyViewHolder extends FastAdapter.ViewHolder<ItemHourlyDB> {
    View view;
    Context context;
    WeatherHourlyItemBinding binding;

    MyViewHolder(View view) {
      super(view);
      binding = WeatherHourlyItemBinding.bind(view);
      this.view = view;
      this.context = view.getContext();
    }

    @Override
    public void bindView(@NonNull ItemHourlyDB item, @NonNull List<Object> payloads) {
      Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
      calendar.setTimeInMillis(item.getDt() * 1000L);
      if (item.getTemp() < 0 && item.getTemp() > -0.5) {
        item.setTemp(0);
      }
      binding.timeTextView.setText(AppUtil.getTime(calendar, context));
      binding.tempTextView.setText(String.format(Locale.getDefault(), "%.0f°", item.getTemp()));
      AppUtil.setWeatherIcon(context, binding.weatherImageView, item.weatherCode);
    }

    @Override
    public void unbindView(@NonNull ItemHourlyDB item) {

    }

  }
}
