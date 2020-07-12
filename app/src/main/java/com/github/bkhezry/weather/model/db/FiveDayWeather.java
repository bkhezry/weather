package com.github.bkhezry.weather.model.db;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.databinding.WeatherDayItemBinding;
import com.github.bkhezry.weather.utils.AppUtil;
import com.github.bkhezry.weather.utils.Constants;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class FiveDayWeather extends AbstractItem<FiveDayWeather, FiveDayWeather.MyViewHolder> implements Parcelable {
  @Id
  private long id;
  private int dt;
  private double temp;
  private double minTemp;
  private double maxTemp;
  private int weatherId;
  private long timestampStart;
  private long timestampEnd;
  private @ColorInt
  int color;
  private @ColorInt
  int colorAlpha;

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

  public double getTemp() {
    return temp;
  }

  public void setTemp(double temp) {
    this.temp = temp;
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

  public int getWeatherId() {
    return weatherId;
  }

  public void setWeatherId(int weatherId) {
    this.weatherId = weatherId;
  }

  public long getTimestampStart() {
    return timestampStart;
  }

  public void setTimestampStart(long timestampStart) {
    this.timestampStart = timestampStart;
  }

  public long getTimestampEnd() {
    return timestampEnd;
  }

  public void setTimestampEnd(long timestampEnd) {
    this.timestampEnd = timestampEnd;
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
  }

  public int getColorAlpha() {
    return colorAlpha;
  }

  public void setColorAlpha(int colorAlpha) {
    this.colorAlpha = colorAlpha;
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
    return R.layout.weather_day_item;
  }

  protected static class MyViewHolder extends FastAdapter.ViewHolder<FiveDayWeather> {
    Context context;
    WeatherDayItemBinding binding;

    MyViewHolder(View view) {
      super(view);
      binding = WeatherDayItemBinding.bind(view);
      context = view.getContext();
    }

    @Override
    public void bindView(@NonNull FiveDayWeather item, @NonNull List<Object> payloads) {
      binding.cardView.setCardBackgroundColor(item.getColor());
      int[] colors = {
          Color.TRANSPARENT,
          item.getColorAlpha(),
          Color.TRANSPARENT
      };
      Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
      calendar.setTimeInMillis(item.getDt() * 1000L);
      if (AppUtil.isRTL(context)) {
        binding.dayNameTextView.setText(Constants.DAYS_OF_WEEK_PERSIAN[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
      } else {
        binding.dayNameTextView.setText(Constants.DAYS_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
      }
      if (item.maxTemp < 0 && item.maxTemp > -0.5) {
        item.maxTemp = 0;
      }
      if (item.minTemp < 0 && item.minTemp > -0.5) {
        item.minTemp = 0;
      }
      if (item.temp < 0 && item.temp > -0.5) {
        item.temp = 0;
      }
      binding.tempTextView.setText(String.format(Locale.getDefault(), "%.0f°", item.getTemp()));
      binding.minTempTextView.setText(String.format(Locale.getDefault(), "%.0f°", item.getMinTemp()));
      binding.maxTempTextView.setText(String.format(Locale.getDefault(), "%.0f°", item.getMaxTemp()));
      AppUtil.setWeatherIcon(context, binding.weatherImageView, item.weatherId);
      GradientDrawable shape = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
      shape.setShape(GradientDrawable.OVAL);
      binding.shadowView.setBackground(shape);
    }

    @Override
    public void unbindView(@NonNull FiveDayWeather item) {

    }

  }

  public static final Parcelable.Creator<FiveDayWeather> CREATOR = new Parcelable.Creator<FiveDayWeather>() {
    @Override
    public FiveDayWeather createFromParcel(Parcel source) {
      return new FiveDayWeather(source);
    }

    @Override
    public FiveDayWeather[] newArray(int size) {
      return new FiveDayWeather[size];
    }
  };

  public FiveDayWeather() {
  }

  protected FiveDayWeather(Parcel in) {
    this.id = in.readLong();
    this.dt = in.readInt();
    this.temp = in.readDouble();
    this.minTemp = in.readDouble();
    this.maxTemp = in.readDouble();
    this.weatherId = in.readInt();
    this.timestampStart = in.readLong();
    this.timestampEnd = in.readLong();
    this.color = in.readInt();
    this.colorAlpha = in.readInt();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(this.id);
    dest.writeInt(this.dt);
    dest.writeDouble(this.temp);
    dest.writeDouble(this.minTemp);
    dest.writeDouble(this.maxTemp);
    dest.writeInt(this.weatherId);
    dest.writeLong(this.timestampStart);
    dest.writeLong(this.timestampEnd);
    dest.writeInt(this.color);
    dest.writeInt(this.colorAlpha);
  }
}
