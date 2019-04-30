package com.github.bkhezry.weather.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.model.WeatherCollection;
import com.github.bkhezry.weather.model.fivedayweather.ItemHourly;
import com.github.bkhezry.weather.utils.AppUtil;
import com.github.bkhezry.weather.utils.Constants;
import com.google.android.material.card.MaterialCardView;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HourlyFragment extends DialogFragment {
  @BindView(R.id.card_view)
  MaterialCardView cardView;
  @BindView(R.id.day_name_text_view)
  AppCompatTextView dayNameTextView;
  @BindView(R.id.weather_image_view)
  AppCompatImageView weatherImageView;
  @BindView(R.id.temp_text_view)
  AppCompatTextView tempTextView;
  @BindView(R.id.min_temp_text_view)
  AppCompatTextView minTempTextView;
  @BindView(R.id.max_temp_text_view)
  AppCompatTextView maxTempTextView;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  private WeatherCollection weatherCollection;
  private FastAdapter<ItemHourly> mFastAdapter;
  private ItemAdapter<ItemHourly> mItemAdapter;


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_hourly,
        container, false);
    ButterKnife.bind(this, view);
    setVariables();
    initRecyclerView();
    setItemHourly();
    return view;
  }

  private void setVariables() {
    cardView.setCardBackgroundColor(weatherCollection.getColor());
    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    calendar.setTimeInMillis(weatherCollection.getListItem().getDt() * 1000L);
    dayNameTextView.setText(Constants.DAYS_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
    tempTextView.setText(String.format("%s°", weatherCollection.getListItem().getTemp().getDay()));
    minTempTextView.setText(String.format("%s°", weatherCollection.getListItem().getTemp().getMin()));
    maxTempTextView.setText(String.format("%s°", weatherCollection.getListItem().getTemp().getMax()));
    int weatherCode = weatherCollection.getListItem().getWeather().get(0).getId();
    AppUtil.setWeatherIcon(getActivity(), weatherImageView, weatherCode);
  }

  private void initRecyclerView() {
    LinearLayoutManager layoutManager
        = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
    recyclerView.setLayoutManager(layoutManager);
    mItemAdapter = new ItemAdapter<>();
    mFastAdapter = FastAdapter.with(mItemAdapter);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(mFastAdapter);
  }

  private void setItemHourly() {
    mItemAdapter.clear();
    mItemAdapter.add(weatherCollection.getListItemHourlies());
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(true);
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
    dialog.getWindow().setAttributes(lp);
    return dialog;
  }

  public void setWeatherCollection(WeatherCollection weatherCollection) {
    this.weatherCollection = weatherCollection;
  }
}
