package com.github.bkhezry.weather.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.model.WeatherCollection;
import com.github.bkhezry.weather.model.currentweather.CurrentWeatherResponse;
import com.github.bkhezry.weather.model.daysweather.ListItem;
import com.github.bkhezry.weather.model.daysweather.MultipleDaysWeatherResponse;
import com.github.bkhezry.weather.model.fivedayweather.FiveDayResponse;
import com.github.bkhezry.weather.model.fivedayweather.ItemHourly;
import com.github.bkhezry.weather.service.ApiService;
import com.github.bkhezry.weather.ui.fragment.HourlyFragment;
import com.github.bkhezry.weather.utils.ApiClient;
import com.github.bkhezry.weather.utils.AppUtil;
import com.github.bkhezry.weather.utils.Constants;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.location_name_text_view)
  AppCompatTextView locationNameTextView;
  @BindView(R.id.temp_text_view)
  AppCompatTextView tempTextView;
  @BindView(R.id.description_text_view)
  AppCompatTextView descriptionTextView;
  @BindView(R.id.humidity_text_view)
  AppCompatTextView humidityTextView;
  @BindView(R.id.weather_image_view)
  AppCompatImageView weatherImageView;
  @BindArray(R.array.mdcolor_500)
  @ColorInt
  int[] colors;
  @BindArray(R.array.mdcolor_500_alpha)
  @ColorInt
  int[] colorsAlpha;
  private FastAdapter<WeatherCollection> mFastAdapter;
  private ItemAdapter<WeatherCollection> mItemAdapter;
  private CompositeDisposable disposable = new CompositeDisposable();
  private String cityName = "Saqqez, IR";
  private String defaultLang = "en";
  private List<WeatherCollection> weatherCollections;
  private ApiService apiService;
  private WeatherCollection todayWeatherCollection;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    apiService = ApiClient.getClient(getApplicationContext()).create(ApiService.class);
    initValues();
    initRecyclerView();
    getCurrentWeather();
    getFiveDaysWeather();
  }

  private void initValues() {
    locationNameTextView.setText(cityName);
  }

  private void getCurrentWeather() {
    disposable.add(
        apiService.getCurrentWeather(
            cityName, Constants.UNITS, defaultLang, Constants.APP_ID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<CurrentWeatherResponse>() {
              @Override
              public void onSuccess(CurrentWeatherResponse currentWeatherResponse) {
                handleCurrentWeather(currentWeatherResponse);
              }

              @Override
              public void onError(Throwable e) {
                Log.e("MainActivity", "onError: " + e.getMessage());
              }
            })

    );
  }

  private void handleCurrentWeather(CurrentWeatherResponse response) {
    tempTextView.setText(String.format("%s°", response.getMain().getTemp()));
    if (response.getWeather().size() != 0) {
      descriptionTextView.setText(response.getWeather().get(0).getMain());
      AppUtil.setWeatherIcon(getApplicationContext(), weatherImageView, response.getWeather().get(0).getId());
    }
    humidityTextView.setText(String.format(Locale.getDefault(), "%d%%", response.getMain().getHumidity()));
  }

  private void getFiveDaysWeather() {
    disposable.add(
        apiService.getMultipleDaysWeather(
            cityName, Constants.UNITS, defaultLang, 5, Constants.APP_ID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<MultipleDaysWeatherResponse>() {
              @Override
              public void onSuccess(MultipleDaysWeatherResponse response) {
                handleFiveDayResponse(response);
              }

              @Override
              public void onError(Throwable e) {
                Log.e("MainActivity", "onError: " + e.getMessage());
              }
            })

    );
  }

  private void handleFiveDayResponse(MultipleDaysWeatherResponse response) {
    weatherCollections = new ArrayList<>();
    List<ListItem> list = response.getList();
    int day = 0;
    for (ListItem item : list) {
      Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"));
      Calendar newCalendar = AppUtil.addDays(calendar, day);
      WeatherCollection weatherCollection = new WeatherCollection();
      weatherCollection.setListItem(item);
      weatherCollection.setColor(colors[day]);
      weatherCollection.setColorAlpha(colorsAlpha[day]);
      weatherCollection.setTimestampStart(AppUtil.getStartOfDayTimestamp(newCalendar));
      weatherCollection.setTimestampEnd(AppUtil.getEndOfDayTimestamp(newCalendar));
      weatherCollections.add(weatherCollection);
      day++;
    }
    getFiveDaysHourlyWeather();
  }

  private void getFiveDaysHourlyWeather() {
    disposable.add(
        apiService.getFiveDaysWeather(
            cityName, Constants.UNITS, defaultLang, Constants.APP_ID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<FiveDayResponse>() {
              @Override
              public void onSuccess(FiveDayResponse response) {
                handleFiveDayHourlyResponse(response);
              }

              @Override
              public void onError(Throwable e) {
                Log.e("MainActivity", "onError: " + e.getMessage());
              }
            })

    );
  }

  private void handleFiveDayHourlyResponse(FiveDayResponse response) {
    for (WeatherCollection weatherCollection : weatherCollections) {
      ArrayList<ItemHourly> listItemHourlies = new ArrayList<>(response.getList());
      for (ItemHourly itemHourly : listItemHourlies) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"));
        calendar.setTimeInMillis(itemHourly.getDt() * 1000L);
        if (calendar.getTimeInMillis()
            <= weatherCollection.getTimestampEnd()
            && calendar.getTimeInMillis()
            > weatherCollection.getTimestampStart()) {
          weatherCollection.addListItemHourlies(itemHourly);
        }
      }
    }
    todayWeatherCollection = weatherCollections.remove(0);
    mItemAdapter.clear();
    mItemAdapter.add(weatherCollections);
  }


  private void initRecyclerView() {
    LinearLayoutManager layoutManager
        = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    recyclerView.setLayoutManager(layoutManager);
    mItemAdapter = new ItemAdapter<>();
    mFastAdapter = FastAdapter.with(mItemAdapter);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(mFastAdapter);
    mFastAdapter.withOnClickListener(new OnClickListener<WeatherCollection>() {
      @Override
      public boolean onClick(@Nullable View v, @NonNull IAdapter<WeatherCollection> adapter, @NonNull WeatherCollection item, int position) {
        HourlyFragment hourlyFragment = new HourlyFragment();
        hourlyFragment.setWeatherCollection(item);
        AppUtil.showFragment(hourlyFragment, getSupportFragmentManager(), true);
        return true;
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    disposable.dispose();
  }
}
