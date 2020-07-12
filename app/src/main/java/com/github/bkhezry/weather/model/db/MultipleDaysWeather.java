package com.github.bkhezry.weather.model.db;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.databinding.MultipleDaysItemBinding;
import com.github.bkhezry.weather.utils.AppUtil;
import com.github.bkhezry.weather.utils.Constants;
import com.github.bkhezry.weather.utils.DateConverter;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class MultipleDaysWeather extends AbstractItem<MultipleDaysWeather, MultipleDaysWeather.MyViewHolder> {
  @Id
  private long id;
  private int dt;
  private int weatherId;
  private double minTemp;
  private double maxTemp;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getDt() {
    return dt;
  }

  public void setDt(int dt) {
    this.dt = dt;
  }

  public int getWeatherId() {
    return weatherId;
  }

  public void setWeatherId(int weatherId) {
    this.weatherId = weatherId;
  }

  public double getMinTemp() {
    return minTemp;
  }

  public void setMinTemp(double minTemp) {
    this.minTemp = minTemp;
  }

  public double getMaxTemp() {
    return maxTemp;
  }

  public void setMaxTemp(double maxTemp) {
    this.maxTemp = maxTemp;
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

  protected static class MyViewHolder extends FastAdapter.ViewHolder<MultipleDaysWeather> {
    Context context;
    View view;
    MultipleDaysItemBinding binding;

    MyViewHolder(View view) {
      super(view);
      binding = MultipleDaysItemBinding.bind(view);
      this.view = view;
      this.context = view.getContext();
    }

    @Override
    public void bindView(@NonNull MultipleDaysWeather item, @NonNull List<Object> payloads) {
      Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
      calendar.setTimeInMillis(item.getDt() * 1000L);
      if (AppUtil.isRTL(context)) {
        DateConverter converter = new DateConverter(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH));
        binding.dayNameTextView.setText(Constants.DAYS_OF_WEEK_PERSIAN[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        binding.dateTextView.setText(String.format(Locale.getDefault(), "%d %s", converter.getIranianDay()
            , Constants.MONTH_NAME_PERSIAN[converter.getIranianMonth() - 1]));
      } else {
        binding.dayNameTextView.setText(Constants.DAYS_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        binding.dateTextView.setText(String.format(Locale.getDefault(), "%s %d",
            Constants.MONTH_NAME[calendar.get(Calendar.MONTH)], calendar.get(Calendar.DAY_OF_MONTH)));
      }
      if (item.maxTemp < 0 && item.maxTemp > -0.5) {
        item.maxTemp = 0;
      }
      if (item.minTemp < 0 && item.minTemp > -0.5) {
        item.minTemp = 0;
      }
      binding.minTempTextView.setText(String.format(Locale.getDefault(), "%.0f°", item.getMinTemp()));
      binding.maxTempTextView.setText(String.format(Locale.getDefault(), "%.0f°", item.getMaxTemp()));
      AppUtil.setWeatherIcon(context, binding.weatherImageView, item.getWeatherId());
    }

    @Override
    public void unbindView(@NonNull MultipleDaysWeather item) {

    }

  }
}
