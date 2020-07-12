package com.github.bkhezry.weather.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.bkhezry.weather.databinding.ActivityHourlyBinding;
import com.github.bkhezry.weather.model.db.FiveDayWeather;
import com.github.bkhezry.weather.model.db.ItemHourlyDB;
import com.github.bkhezry.weather.utils.AppUtil;
import com.github.bkhezry.weather.utils.Constants;
import com.github.bkhezry.weather.utils.DbUtil;
import com.github.bkhezry.weather.utils.ElasticDragDismissFrameLayout;
import com.github.bkhezry.weather.utils.MyApplication;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.query.Query;
import io.objectbox.reactive.DataObserver;

public class HourlyActivity extends BaseActivity {
  private FastAdapter<ItemHourlyDB> mFastAdapter;
  private ItemAdapter<ItemHourlyDB> mItemAdapter;
  private FiveDayWeather fiveDayWeather;
  private Box<ItemHourlyDB> itemHourlyDBBox;
  private Typeface typeface;
  private ActivityHourlyBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityHourlyBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    setVariables();
    initRecyclerView();
    showItemHourlyDB();
    setupDismissFrameLayout();
  }

  private void setupDismissFrameLayout() {
    binding.draggableFrame.addListener(new ElasticDragDismissFrameLayout.SystemChromeFader(this) {
      @Override
      public void onDragDismissed() {
        super.onDragDismissed();
        finishAfterTransition();
      }
    });
  }

  private void setVariables() {
    Intent intent = getIntent();
    fiveDayWeather = intent.getParcelableExtra(Constants.FIVE_DAY_WEATHER_ITEM);
    BoxStore boxStore = MyApplication.getBoxStore();
    itemHourlyDBBox = boxStore.boxFor(ItemHourlyDB.class);
    binding.cardView.setCardBackgroundColor(fiveDayWeather.getColor());
    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    calendar.setTimeInMillis(fiveDayWeather.getDt() * 1000L);
    if (AppUtil.isRTL(this)) {
      binding.dayNameTextView.setText(Constants.DAYS_OF_WEEK_PERSIAN[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
    } else {
      binding.dayNameTextView.setText(Constants.DAYS_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
    }
    if (fiveDayWeather.getMaxTemp() < 0 && fiveDayWeather.getMaxTemp() > -0.5) {
      fiveDayWeather.setMaxTemp(0);
    }
    if (fiveDayWeather.getMinTemp() < 0 && fiveDayWeather.getMinTemp() > -0.5) {
      fiveDayWeather.setMinTemp(0);
    }
    if (fiveDayWeather.getTemp() < 0 && fiveDayWeather.getTemp() > -0.5) {
      fiveDayWeather.setTemp(0);
    }
    binding.tempTextView.setText(String.format(Locale.getDefault(), "%.0f°", fiveDayWeather.getTemp()));
    binding.minTempTextView.setText(String.format(Locale.getDefault(), "%.0f°", fiveDayWeather.getMinTemp()));
    binding.maxTempTextView.setText(String.format(Locale.getDefault(), "%.0f°", fiveDayWeather.getMaxTemp()));
    binding.animationView.setAnimation(AppUtil.getWeatherAnimation(fiveDayWeather.getWeatherId()));
    binding.animationView.playAnimation();
    typeface = Typeface.createFromAsset(getAssets(), "fonts/Vazir.ttf");
  }

  private void initRecyclerView() {
    LinearLayoutManager layoutManager
        = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    binding.recyclerView.setLayoutManager(layoutManager);
    mItemAdapter = new ItemAdapter<>();
    mFastAdapter = FastAdapter.with(mItemAdapter);
    binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
    binding.recyclerView.setAdapter(mFastAdapter);
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
    if (AppUtil.isRTL(this)) {
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
    binding.chart.getDescription().setEnabled(false);
    binding.chart.getAxisLeft().setDrawLabels(false);
    binding.chart.getAxisRight().setDrawLabels(false);
    binding.chart.getXAxis().setDrawLabels(false);
    binding.chart.getLegend().setEnabled(false);   // Hide the legend
    binding.chart.getXAxis().setDrawGridLines(false);
    binding.chart.getAxisLeft().setDrawGridLines(false);
    binding.chart.getAxisRight().setDrawGridLines(false);
    binding.chart.getAxisLeft().setDrawAxisLine(false);
    binding.chart.getAxisRight().setDrawAxisLine(false);
    binding.chart.getXAxis().setDrawAxisLine(false);
    binding.chart.setScaleEnabled(false);
    binding.chart.setData(lineData);
    binding.chart.animateY(1000);
  }
}
