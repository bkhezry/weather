package com.github.bkhezry.weather.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.model.db.FiveDayWeather;
import com.github.bkhezry.weather.model.db.ItemHourlyDB;
import com.github.bkhezry.weather.utils.AppUtil;
import com.github.bkhezry.weather.utils.Constants;
import com.github.bkhezry.weather.utils.DbUtil;
import com.github.bkhezry.weather.utils.MyApplication;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.card.MaterialCardView;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.query.Query;
import io.objectbox.reactive.DataObserver;

public class HourlyFragment extends DialogFragment {
  @BindView(R.id.card_view)
  MaterialCardView cardView;
  @BindView(R.id.day_name_text_view)
  AppCompatTextView dayNameTextView;
  @BindView(R.id.temp_text_view)
  AppCompatTextView tempTextView;
  @BindView(R.id.min_temp_text_view)
  AppCompatTextView minTempTextView;
  @BindView(R.id.max_temp_text_view)
  AppCompatTextView maxTempTextView;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.animation_view)
  LottieAnimationView animationView;
  @BindView(R.id.chart)
  LineChart chart;
  private FastAdapter<ItemHourlyDB> mFastAdapter;
  private ItemAdapter<ItemHourlyDB> mItemAdapter;
  private FiveDayWeather fiveDayWeather;
  private Box<ItemHourlyDB> itemHourlyDBBox;
  private Activity activity;
  private Typeface typeface;


  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_hourly,
        container, false);
    ButterKnife.bind(this, view);
    setVariables();
    initRecyclerView();
    showItemHourlyDB();
    return view;
  }

  private void setVariables() {
    BoxStore boxStore = MyApplication.getBoxStore();
    itemHourlyDBBox = boxStore.boxFor(ItemHourlyDB.class);
    activity = getActivity();
    cardView.setCardBackgroundColor(fiveDayWeather.getColor());
    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    calendar.setTimeInMillis(fiveDayWeather.getDt() * 1000L);
    if (AppUtil.isRTL(activity)) {
      dayNameTextView.setText(Constants.DAYS_OF_WEEK_PERSIAN[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
    } else {
      dayNameTextView.setText(Constants.DAYS_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
    }
    tempTextView.setText(String.format(Locale.getDefault(), "%.0f°", fiveDayWeather.getTemp()));
    minTempTextView.setText(String.format(Locale.getDefault(), "%.0f°", fiveDayWeather.getMinTemp()));
    maxTempTextView.setText(String.format(Locale.getDefault(), "%.0f°", fiveDayWeather.getMaxTemp()));
    animationView.setAnimation(AppUtil.getWeatherAnimation(fiveDayWeather.getWeatherId()));
    animationView.playAnimation();
    typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/Vazir.ttf");
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

  private void showItemHourlyDB() {
    Query<ItemHourlyDB> query = DbUtil.getItemHourlyDBQuery(itemHourlyDBBox, fiveDayWeather.getId());
    query.subscribe().on(AndroidScheduler.mainThread())
        .observer(new DataObserver<List<ItemHourlyDB>>() {
          @Override
          public void onData(@NonNull List<ItemHourlyDB> data) {
            if (data.size() > 0) {
              mItemAdapter.clear();
              mItemAdapter.add(data);
              setChartValues(data);
            }
          }
        });
  }

  private void setChartValues(List<ItemHourlyDB> itemHourlyDBList) {
    List<Entry> entries = new ArrayList<>();
    int i = 0;
    if (AppUtil.isRTL(activity)) {
      int j = itemHourlyDBList.size() - 1;
      while (j >= 0) {
        entries.add(new Entry(i, (float) itemHourlyDBList.get(j).getTemp()));
        i++;
        j--;
      }
    } else {
      for (ItemHourlyDB itemHourlyDB : itemHourlyDBList) {
        entries.add(new Entry(i, (float) itemHourlyDB.getTemp()));
        i++;
      }
    }
    LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
    dataSet.setLineWidth(4f);
    dataSet.setCircleRadius(7f);
    dataSet.setHighlightEnabled(false);
    dataSet.setCircleColor(Color.parseColor("#33b5e5"));
    dataSet.setValueTextSize(12);
    dataSet.setValueTextColor(Color.WHITE);
    dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    dataSet.setValueTypeface(typeface);
    dataSet.setValueFormatter(new ValueFormatter() {
      @Override
      public String getFormattedValue(float value) {
        return String.format(Locale.getDefault(), "%.0f", value);
      }
    });
    LineData lineData = new LineData(dataSet);
    chart.getDescription().setEnabled(false);
    chart.getAxisLeft().setDrawLabels(false);
    chart.getAxisRight().setDrawLabels(false);
    chart.getXAxis().setDrawLabels(false);
    chart.getLegend().setEnabled(false);   // Hide the legend

    chart.getXAxis().setDrawGridLines(false);
    chart.getAxisLeft().setDrawGridLines(false);
    chart.getAxisRight().setDrawGridLines(false);
    chart.getAxisLeft().setDrawAxisLine(false);
    chart.getAxisRight().setDrawAxisLine(false);
    chart.getXAxis().setDrawAxisLine(false);
    chart.setScaleEnabled(false);
    chart.setData(lineData);
    chart.animateY(1000);
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

  public void setFiveDayWeather(FiveDayWeather fiveDayWeather) {
    this.fiveDayWeather = fiveDayWeather;
  }

  @OnClick(R.id.close_button)
  void close() {
    dismiss();
    if (getFragmentManager() != null) {
      getFragmentManager().popBackStack();
    }
  }
}
