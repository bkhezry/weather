package com.github.bkhezry.weather.utils;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
  private static Retrofit retrofit = null;
  private static int REQUEST_TIMEOUT = 60;
  private static OkHttpClient okHttpClient;

  /**
   * @return retrofit instance {@link Retrofit}
   */
  public static Retrofit getClient() {

    if (okHttpClient == null)
      initOkHttp();

    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
          .baseUrl(Constants.BASE_URL)
          .client(okHttpClient)
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .build();
    }
    return retrofit;
  }

  /**
   * init instance of {@link OkHttpClient}
   */
  private static void initOkHttp() {
    OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
        .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    //httpClient.addInterceptor(interceptor);

    httpClient.addInterceptor(new Interceptor() {
      @Override
      public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json");

        Request request = requestBuilder.build();
        return chain.proceed(request);
      }
    });

    okHttpClient = httpClient.build();
  }
}
