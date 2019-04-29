package com.github.bkhezry.weather.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.model.daysweather.ListItem;
import com.github.bkhezry.weather.model.fivedayweather.ListItemHourly;
import com.github.bkhezry.weather.utils.AppUtil;
import com.github.bkhezry.weather.utils.Constants;
import com.google.android.material.card.MaterialCardView;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherCollection extends AbstractItem<WeatherCollection, WeatherCollection.MyViewHolder> {
  private List<ListItemHourly> listItemHourlies;
  private ListItem listItem;
  private long timestampStart;
  private long timestampEnd;
  private @ColorInt
  int color;
  private @ColorInt
  int colorAlpha;

  public List<ListItemHourly> getListItemHourlies() {
    return listItemHourlies;
  }

  public void setListItemHourlies(List<ListItemHourly> listItemHourlies) {
    this.listItemHourlies = listItemHourlies;
  }

  public void addListItemHourlies(ListItemHourly listItem) {
    if (this.listItemHourlies == null) {
      this.listItemHourlies = new ArrayList<>();
    }
    this.listItemHourlies.add(listItem);
  }

  public ListItem getListItem() {
    return listItem;
  }

  public void setListItem(ListItem listItem) {
    this.listItem = listItem;
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

  protected static class MyViewHolder extends FastAdapter.ViewHolder<WeatherCollection> {
    Context context;
    View view;
    @BindView(R.id.day_name_text_view)
    AppCompatTextView dayNameTextView;
    @BindView(R.id.temp_text_view)
    AppCompatTextView tempTextView;
    @BindView(R.id.min_temp_text_view)
    AppCompatTextView minTempTextView;
    @BindView(R.id.max_temp_text_view)
    AppCompatTextView maxTempTextView;
    @BindView(R.id.weather_image_view)
    AppCompatImageView weatherImageView;
    @BindView(R.id.card_view)
    MaterialCardView cardView;
    @BindView(R.id.shadow_view)
    View shadowView;

    MyViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      this.view = view;
      this.context = view.getContext();
    }

    @Override
    public void bindView(@NonNull WeatherCollection item, @NonNull List<Object> payloads) {
      cardView.setCardBackgroundColor(item.getColor());
      int[] colors = {
          Color.TRANSPARENT,
          item.getColorAlpha(),
          Color.TRANSPARENT
      };
      Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
      calendar.setTimeInMillis(item.getListItem().getDt() * 1000L);
      dayNameTextView.setText(Constants.DAYS_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
      tempTextView.setText(String.format("%s°", item.getListItem().getTemp().getDay()));
      minTempTextView.setText(String.format("%s°", item.getListItem().getTemp().getMin()));
      maxTempTextView.setText(String.format("%s°", item.getListItem().getTemp().getMax()));
      int weatherCode = item.getListItem().getWeather().get(0).getId();
      AppUtil.setWeatherIcon(context, weatherImageView, weatherCode);
      GradientDrawable shape = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
      shape.setShape(GradientDrawable.OVAL);

      shadowView.setBackground(shape);
    }

    @Override
    public void unbindView(@NonNull WeatherCollection item) {

    }

  }
}
