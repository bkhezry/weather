package com.github.bkhezry.weather.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.model.WeatherCollection;
import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HourlyFragment extends DialogFragment {
  @BindView(R.id.card_view)
  MaterialCardView cardView;
  WeatherCollection weatherCollection;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_hourly,
        container, false);
    ButterKnife.bind(this, view);
    cardView.setCardBackgroundColor(weatherCollection.getColor());
    return view;
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

  @Override
  public void onActivityCreated(Bundle arg0) {
    super.onActivityCreated(arg0);

  }

  public void setWeatherCollection(WeatherCollection weatherCollection) {
    this.weatherCollection = weatherCollection;
  }
}
