package com.github.bkhezry.weather.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.model.CityInfo;
import com.github.bkhezry.weather.model.currentweather.CurrentWeatherResponse;
import com.github.bkhezry.weather.model.daysweather.ListItem;
import com.github.bkhezry.weather.model.daysweather.MultipleDaysWeatherResponse;
import com.github.bkhezry.weather.model.db.CurrentWeather;
import com.github.bkhezry.weather.model.db.FiveDayWeather;
import com.github.bkhezry.weather.model.db.ItemHourlyDB;
import com.github.bkhezry.weather.model.fivedayweather.FiveDayResponse;
import com.github.bkhezry.weather.model.fivedayweather.ItemHourly;
import com.github.bkhezry.weather.service.ApiService;
import com.github.bkhezry.weather.ui.fragment.AboutFragment;
import com.github.bkhezry.weather.ui.fragment.MultipleDaysFragment;
import com.github.bkhezry.weather.utils.ApiClient;
import com.github.bkhezry.weather.utils.AppUtil;
import com.github.bkhezry.weather.utils.Constants;
import com.github.bkhezry.weather.utils.DbUtil;
import com.github.bkhezry.weather.utils.MyApplication;
import com.github.bkhezry.weather.utils.SnackbarUtil;
import com.github.bkhezry.weather.utils.TextViewFactory;
import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
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
import butterknife.OnClick;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.query.Query;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataSubscriptionList;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.temp_text_view)
  TextSwitcher tempTextView;
  @BindView(R.id.description_text_view)
  TextSwitcher descriptionTextView;
  @BindView(R.id.humidity_text_view)
  TextSwitcher humidityTextView;
  @BindArray(R.array.mdcolor_500)
  @ColorInt
  int[] colors;
  @BindArray(R.array.mdcolor_500_alpha)
  @ColorInt
  int[] colorsAlpha;
  @BindView(R.id.animation_view)
  LottieAnimationView animationView;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.search_view)
  MaterialSearchView searchView;
  @BindView(R.id.city_name_text_view)
  AppCompatTextView cityNameTextView;
  @BindView(R.id.wind_text_view)
  TextSwitcher windTextView;
  @BindView(R.id.swipe_container)
  SwipeRefreshLayout swipeContainer;
  @BindView(R.id.no_city_image_view)
  AppCompatImageView noCityImageView;
  @BindView(R.id.empty_layout)
  RelativeLayout emptyLayout;
  @BindView(R.id.nested_scroll_view)
  NestedScrollView nestedScrollView;
  @BindView(R.id.bar)
  BottomAppBar bar;
  private FastAdapter<FiveDayWeather> mFastAdapter;
  private ItemAdapter<FiveDayWeather> mItemAdapter;
  private CompositeDisposable disposable = new CompositeDisposable();
  private String defaultLang = "en";
  private List<FiveDayWeather> fiveDayWeathers;
  private ApiService apiService;
  private FiveDayWeather todayFiveDayWeather;
  private Prefser prefser;
  private Box<CurrentWeather> currentWeatherBox;
  private Box<FiveDayWeather> fiveDayWeatherBox;
  private Box<ItemHourlyDB> itemHourlyDBBox;
  private DataSubscriptionList subscriptions = new DataSubscriptionList();
  private boolean isLoad = false;
  private CityInfo cityInfo;
  private String apiKey;
  private Typeface typeface;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    initSearchView();
    initValues();
    setupTextSwitchers();
    initRecyclerView();
    showStoredCurrentWeather();
    showStoredFiveDayWeather();
    checkLastUpdate();
  }

  private void initSearchView() {
    searchView.setVoiceSearch(false);
    searchView.setHint(getString(R.string.search_label));
    searchView.setCursorDrawable(R.drawable.custom_curosr);
    searchView.setEllipsize(true);
    searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        requestWeather(query, true);
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });
  }

  private void initValues() {
    prefser = new Prefser(this);
    apiService = ApiClient.getClient().create(ApiService.class);
    BoxStore boxStore = MyApplication.getBoxStore();
    currentWeatherBox = boxStore.boxFor(CurrentWeather.class);
    fiveDayWeatherBox = boxStore.boxFor(FiveDayWeather.class);
    itemHourlyDBBox = boxStore.boxFor(ItemHourlyDB.class);
    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light);
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

      @Override
      public void onRefresh() {
        cityInfo = prefser.get(Constants.CITY_INFO, CityInfo.class, null);
        if (cityInfo != null) {
          long lastStored = prefser.get(Constants.LAST_STORED_CURRENT, Long.class, 0L);
          if (AppUtil.isTimePass(lastStored)) {
            requestWeather(cityInfo.getName(), false);
          } else {
            swipeContainer.setRefreshing(false);
          }
        } else {
          swipeContainer.setRefreshing(false);
        }
      }

    });
    bar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showAboutFragment();
      }
    });
    typeface = Typeface.createFromAsset(getAssets(), "fonts/Vazir.ttf");
  }

  private void setupTextSwitchers() {
    tempTextView.setFactory(new TextViewFactory(MainActivity.this, R.style.TempTextView, true, typeface));
    tempTextView.setInAnimation(MainActivity.this, R.anim.slide_in_right);
    tempTextView.setOutAnimation(MainActivity.this, R.anim.slide_out_left);
    descriptionTextView.setFactory(new TextViewFactory(MainActivity.this, R.style.DescriptionTextView, true, typeface));
    descriptionTextView.setInAnimation(MainActivity.this, R.anim.slide_in_right);
    descriptionTextView.setOutAnimation(MainActivity.this, R.anim.slide_out_left);
    humidityTextView.setFactory(new TextViewFactory(MainActivity.this, R.style.HumidityTextView, false, typeface));
    humidityTextView.setInAnimation(MainActivity.this, R.anim.slide_in_bottom);
    humidityTextView.setOutAnimation(MainActivity.this, R.anim.slide_out_top);
    windTextView.setFactory(new TextViewFactory(MainActivity.this, R.style.WindSpeedTextView, false, typeface));
    windTextView.setInAnimation(MainActivity.this, R.anim.slide_in_bottom);
    windTextView.setOutAnimation(MainActivity.this, R.anim.slide_out_top);
  }

  private void initRecyclerView() {
    LinearLayoutManager layoutManager
        = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    recyclerView.setLayoutManager(layoutManager);
    mItemAdapter = new ItemAdapter<>();
    mFastAdapter = FastAdapter.with(mItemAdapter);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(mFastAdapter);
    recyclerView.setFocusable(false);
    mFastAdapter.withOnClickListener(new OnClickListener<FiveDayWeather>() {
      @Override
      public boolean onClick(@Nullable View v, @NonNull IAdapter<FiveDayWeather> adapter, @NonNull FiveDayWeather item, int position) {
        Intent intent = new Intent(MainActivity.this, HourlyActivity.class);
        intent.putExtra(Constants.FIVE_DAY_WEATHER_ITEM, item);
        startActivity(intent);
        return true;
      }
    });
  }

  private void showStoredCurrentWeather() {
    Query<CurrentWeather> query = DbUtil.getCurrentWeatherQuery(currentWeatherBox);
    query.subscribe(subscriptions).on(AndroidScheduler.mainThread())
        .observer(new DataObserver<List<CurrentWeather>>() {
          @Override
          public void onData(@NonNull List<CurrentWeather> data) {
            if (data.size() > 0) {
              hideEmptyLayout();
              CurrentWeather currentWeather = data.get(0);
              if (isLoad) {
                tempTextView.setText(String.format(Locale.getDefault(), "%.0f°", currentWeather.getTemp()));
                descriptionTextView.setText(AppUtil.getWeatherStatus(currentWeather.getWeatherId(), AppUtil.isRTL(MainActivity.this)));
                humidityTextView.setText(String.format(Locale.getDefault(), "%d%%", currentWeather.getHumidity()));
                windTextView.setText(String.format(Locale.getDefault(), getResources().getString(R.string.wind_unit_label), currentWeather.getWindSpeed()));
              } else {
                tempTextView.setCurrentText(String.format(Locale.getDefault(), "%.0f°", currentWeather.getTemp()));
                descriptionTextView.setCurrentText(AppUtil.getWeatherStatus(currentWeather.getWeatherId(), AppUtil.isRTL(MainActivity.this)));
                humidityTextView.setCurrentText(String.format(Locale.getDefault(), "%d%%", currentWeather.getHumidity()));
                windTextView.setCurrentText(String.format(Locale.getDefault(), getResources().getString(R.string.wind_unit_label), currentWeather.getWindSpeed()));
              }
              animationView.setAnimation(AppUtil.getWeatherAnimation(currentWeather.getWeatherId()));
              animationView.playAnimation();
            }
          }
        });
  }

  private void showStoredFiveDayWeather() {
    Query<FiveDayWeather> query = DbUtil.getFiveDayWeatherQuery(fiveDayWeatherBox);
    query.subscribe(subscriptions).on(AndroidScheduler.mainThread())
        .observer(new DataObserver<List<FiveDayWeather>>() {
          @Override
          public void onData(@NonNull List<FiveDayWeather> data) {
            if (data.size() > 0) {
              todayFiveDayWeather = data.remove(0);
              mItemAdapter.clear();
              mItemAdapter.add(data);
            }
          }
        });
  }

  private void checkLastUpdate() {
    cityInfo = prefser.get(Constants.CITY_INFO, CityInfo.class, null);
    if (cityInfo != null) {
      cityNameTextView.setText(String.format("%s, %s", cityInfo.getName(), cityInfo.getCountry()));
      if (prefser.contains(Constants.LAST_STORED_CURRENT)) {
        long lastStored = prefser.get(Constants.LAST_STORED_CURRENT, Long.class, 0L);
        if (AppUtil.isTimePass(lastStored)) {
          requestWeather(cityInfo.getName(), false);
        }
      } else {
        requestWeather(cityInfo.getName(), false);
      }
    } else {
      showEmptyLayout();
    }

  }


  private void requestWeather(String cityName, boolean isSearch) {
    if (AppUtil.isNetworkConnected()) {
      getCurrentWeather(cityName, isSearch);
      getFiveDaysWeather(cityName);
    } else {
      SnackbarUtil
          .with(swipeContainer)
          .setMessage(getString(R.string.no_internet_message))
          .setDuration(SnackbarUtil.LENGTH_LONG)
          .showError();
      swipeContainer.setRefreshing(false);
    }
  }

  private void getCurrentWeather(String cityName, boolean isSearch) {
    apiKey = getResources().getString(R.string.open_weather_map_api);
    disposable.add(
        apiService.getCurrentWeather(
            cityName, Constants.UNITS, defaultLang, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<CurrentWeatherResponse>() {
              @Override
              public void onSuccess(CurrentWeatherResponse currentWeatherResponse) {
                isLoad = true;
                storeCurrentWeather(currentWeatherResponse);
                storeCityInfo(currentWeatherResponse);
                swipeContainer.setRefreshing(false);
                if (isSearch) {
                  prefser.remove(Constants.LAST_STORED_MULTIPLE_DAYS);
                }
              }

              @Override
              public void onError(Throwable e) {
                swipeContainer.setRefreshing(false);
                try {
                  HttpException error = (HttpException) e;
                  handleErrorCode(error);
                } catch (Exception exception) {
                  e.printStackTrace();
                }
              }
            })

    );
  }

  private void handleErrorCode(HttpException error) {
    if (error.code() == 404) {
      SnackbarUtil
          .with(swipeContainer)
          .setMessage(getString(R.string.no_city_found_message))
          .setDuration(SnackbarUtil.LENGTH_INDEFINITE)
          .setAction(getResources().getString(R.string.search_label), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              searchView.showSearch();
            }
          })
          .showWarning();

    } else if (error.code() == 401) {
      SnackbarUtil
          .with(swipeContainer)
          .setMessage(getString(R.string.invalid_api_key_message))
          .setDuration(SnackbarUtil.LENGTH_INDEFINITE)
          .setAction(getString(R.string.ok_label), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
          })
          .showError();

    } else {
      SnackbarUtil
          .with(swipeContainer)
          .setMessage(getString(R.string.network_exception_message))
          .setDuration(SnackbarUtil.LENGTH_LONG)
          .setAction(getResources().getString(R.string.retry_label), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (cityInfo != null) {
                requestWeather(cityInfo.getName(), false);
              } else {
                searchView.showSearch();
              }
            }
          })
          .showWarning();
    }
  }

  private void showEmptyLayout() {
    Glide.with(MainActivity.this).load(R.drawable.no_city).into(noCityImageView);
    emptyLayout.setVisibility(View.VISIBLE);
    nestedScrollView.setVisibility(View.GONE);
  }

  private void hideEmptyLayout() {
    emptyLayout.setVisibility(View.GONE);
    nestedScrollView.setVisibility(View.VISIBLE);
  }


  private void storeCurrentWeather(CurrentWeatherResponse response) {
    CurrentWeather currentWeather = new CurrentWeather();
    currentWeather.setTemp(response.getMain().getTemp());
    currentWeather.setHumidity(response.getMain().getHumidity());
    currentWeather.setDescription(response.getWeather().get(0).getDescription());
    currentWeather.setMain(response.getWeather().get(0).getMain());
    currentWeather.setWeatherId(response.getWeather().get(0).getId());
    currentWeather.setWindDeg(response.getWind().getDeg());
    currentWeather.setWindSpeed(response.getWind().getSpeed());
    currentWeather.setStoreTimestamp(System.currentTimeMillis());
    prefser.put(Constants.LAST_STORED_CURRENT, System.currentTimeMillis());
    if (!currentWeatherBox.isEmpty()) {
      currentWeatherBox.removeAll();
      currentWeatherBox.put(currentWeather);
    } else {
      currentWeatherBox.put(currentWeather);
    }
  }

  private void storeCityInfo(CurrentWeatherResponse response) {
    CityInfo cityInfo = new CityInfo();
    cityInfo.setCountry(response.getSys().getCountry());
    cityInfo.setId(response.getId());
    cityInfo.setName(response.getName());
    prefser.put(Constants.CITY_INFO, cityInfo);
    cityNameTextView.setText(String.format("%s, %s", cityInfo.getName(), cityInfo.getCountry()));
  }

  private void getFiveDaysWeather(String cityName) {
    disposable.add(
        apiService.getMultipleDaysWeather(
            cityName, Constants.UNITS, defaultLang, 5, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<MultipleDaysWeatherResponse>() {
              @Override
              public void onSuccess(MultipleDaysWeatherResponse response) {
                handleFiveDayResponse(response, cityName);
              }

              @Override
              public void onError(Throwable e) {
                e.printStackTrace();
              }
            })
    );
  }

  private void handleFiveDayResponse(MultipleDaysWeatherResponse response, String cityName) {
    fiveDayWeathers = new ArrayList<>();
    List<ListItem> list = response.getList();
    int day = 0;
    for (ListItem item : list) {
      Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
      Calendar newCalendar = AppUtil.addDays(calendar, day);
      FiveDayWeather fiveDayWeather = new FiveDayWeather();
      fiveDayWeather.setWeatherId(item.getWeather().get(0).getId());
      fiveDayWeather.setDt(item.getDt());
      fiveDayWeather.setMaxTemp(item.getTemp().getMax());
      fiveDayWeather.setMinTemp(item.getTemp().getMin());
      fiveDayWeather.setTemp(item.getTemp().getDay());
      fiveDayWeather.setColor(colors[day]);
      fiveDayWeather.setColorAlpha(colorsAlpha[day]);
      fiveDayWeather.setTimestampStart(AppUtil.getStartOfDayTimestamp(newCalendar));
      fiveDayWeather.setTimestampEnd(AppUtil.getEndOfDayTimestamp(newCalendar));
      fiveDayWeathers.add(fiveDayWeather);
      day++;
    }
    getFiveDaysHourlyWeather(cityName);
  }

  private void getFiveDaysHourlyWeather(String cityName) {
    disposable.add(
        apiService.getFiveDaysWeather(
            cityName, Constants.UNITS, defaultLang, apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<FiveDayResponse>() {
              @Override
              public void onSuccess(FiveDayResponse response) {
                handleFiveDayHourlyResponse(response);
              }

              @Override
              public void onError(Throwable e) {
                e.printStackTrace();
              }
            })

    );
  }

  private void handleFiveDayHourlyResponse(FiveDayResponse response) {
    if (!fiveDayWeatherBox.isEmpty()) {
      fiveDayWeatherBox.removeAll();
    }
    if (!itemHourlyDBBox.isEmpty()) {
      itemHourlyDBBox.removeAll();
    }
    for (FiveDayWeather fiveDayWeather : fiveDayWeathers) {
      long fiveDayWeatherId = fiveDayWeatherBox.put(fiveDayWeather);
      ArrayList<ItemHourly> listItemHourlies = new ArrayList<>(response.getList());
      for (ItemHourly itemHourly : listItemHourlies) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(itemHourly.getDt() * 1000L);
        if (calendar.getTimeInMillis()
            <= fiveDayWeather.getTimestampEnd()
            && calendar.getTimeInMillis()
            > fiveDayWeather.getTimestampStart()) {
          ItemHourlyDB itemHourlyDB = new ItemHourlyDB();
          itemHourlyDB.setDt(itemHourly.getDt());
          itemHourlyDB.setFiveDayWeatherId(fiveDayWeatherId);
          itemHourlyDB.setTemp(itemHourly.getMain().getTemp());
          itemHourlyDB.setWeatherCode(itemHourly.getWeather().get(0).getId());
          itemHourlyDBBox.put(itemHourlyDB);
        }
      }
    }
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    disposable.dispose();
  }

  @OnClick(R.id.next_days_button)
  public void multipleDays() {
    AppUtil.showFragment(new MultipleDaysFragment(), getSupportFragmentManager(), true);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    MenuItem item = menu.findItem(R.id.action_search);
    searchView.setMenuItem(item);
    return true;
  }

  public void showAboutFragment() {
    AppUtil.showFragment(new AboutFragment(), getSupportFragmentManager(), true);
  }

  @Override
  protected void attachBaseContext(Context base) {
    Context newContext = MyApplication.localeManager.setLocale(base);
    super.attachBaseContext(ViewPumpContextWrapper.wrap(newContext));
  }

  @Override
  public void onBackPressed() {
    if (searchView.isSearchOpen()) {
      searchView.closeSearch();
    } else {
      super.onBackPressed();
    }
  }

  @OnClick(R.id.search_text_view)
  public void handleSearchTextClick() {
    searchView.showSearch();
  }
}
