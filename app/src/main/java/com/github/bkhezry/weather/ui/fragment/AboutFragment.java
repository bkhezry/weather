package com.github.bkhezry.weather.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;

import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.ui.activity.MainActivity;
import com.github.bkhezry.weather.utils.AppUtil;
import com.github.bkhezry.weather.utils.LocaleManager;
import com.github.bkhezry.weather.utils.MyApplication;
import com.github.bkhezry.weather.utils.SharedPreferencesUtil;
import com.github.bkhezry.weather.utils.ViewAnimation;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutFragment extends DialogFragment {

  @BindView(R.id.english_button)
  ExtendedFloatingActionButton englishButton;
  @BindView(R.id.persian_button)
  ExtendedFloatingActionButton persianButton;
  @BindView(R.id.toggle_info_button)
  ImageButton toggleInfoButton;
  @BindView(R.id.expand_layout)
  LinearLayout expandLayout;
  @BindView(R.id.nested_scroll_view)
  NestedScrollView nestedScrollView;
  @BindView(R.id.night_mode_switch)
  SwitchCompat nightModeSwitch;
  private Activity activity;
  private String currentLanguage;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_about,
        container, false);
    ButterKnife.bind(this, view);
    initVariables(view);
    return view;
  }

  private void initVariables(View view) {
    currentLanguage = MyApplication.localeManager.getLanguage();
    activity = getActivity();
    if (activity != null) {
      Drawable drawable = activity.getResources().getDrawable(R.drawable.ic_done_black_24dp);
      String versionName = "";
      try {
        versionName = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
      } catch (PackageManager.NameNotFoundException e) {
        // do nothing
      }
      setTextWithLinks(view.findViewById(R.id.text_application_info), getString(R.string.application_info_text, versionName));
      setTextWithLinks(view.findViewById(R.id.text_developer_info), getString(R.string.developer_info_text));
      setTextWithLinks(view.findViewById(R.id.text_design_api), getString(R.string.design_api_text));
      setTextWithLinks(view.findViewById(R.id.text_libraries), getString(R.string.libraries_text));
      setTextWithLinks(view.findViewById(R.id.text_license), getString(R.string.license_text));
      if (currentLanguage.equals(LocaleManager.LANGUAGE_ENGLISH)) {
        englishButton.setIcon(drawable);
      } else {
        persianButton.setIcon(drawable);
      }
    }
    nightModeSwitch.setChecked(SharedPreferencesUtil.getInstance(activity).isDarkThemeEnabled());
    nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferencesUtil.getInstance(activity).setDarkThemeEnabled(isChecked);
        if (isChecked) {
          AppCompatDelegate.setDefaultNightMode(
              AppCompatDelegate.MODE_NIGHT_YES);
        } else {
          AppCompatDelegate.setDefaultNightMode(
              AppCompatDelegate.MODE_NIGHT_NO);
        }
        activity.recreate();
      }
    });
  }

  private void setTextWithLinks(TextView textView, String htmlText) {
    AppUtil.setTextWithLinks(textView, AppUtil.fromHtml(htmlText));
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


  @OnClick(R.id.close_button)
  void close() {
    dismiss();
    if (getFragmentManager() != null) {
      getFragmentManager().popBackStack();
    }
  }

  @OnClick({R.id.english_button, R.id.persian_button})
  void handleChangeLanguage(View view) {
    switch (view.getId()) {
      case R.id.english_button:
        if (currentLanguage.equals(LocaleManager.LANGUAGE_PERSIAN)) {
          MyApplication.localeManager.setNewLocale(activity, LocaleManager.LANGUAGE_ENGLISH);
          restartActivity();
        }
        break;
      case R.id.persian_button:
        if (currentLanguage.equals(LocaleManager.LANGUAGE_ENGLISH)) {
          MyApplication.localeManager.setNewLocale(activity, LocaleManager.LANGUAGE_PERSIAN);
          restartActivity();
        }
        break;
    }
  }

  private void restartActivity() {
    Intent intent = new Intent(activity, MainActivity.class);
    activity.startActivity(intent);
    activity.finish();
  }

  @OnClick({R.id.toggle_info_button, R.id.toggle_info_layout})
  void toggleInfoLayout() {
    boolean show = toggleArrow(toggleInfoButton);
    if (show) {
      ViewAnimation.expand(expandLayout, new ViewAnimation.AnimListener() {
        @Override
        public void onFinish() {
        }
      });
    } else {
      ViewAnimation.collapse(expandLayout);
    }
  }

  private boolean toggleArrow(View view) {
    if (view.getRotation() == 0) {
      view.animate().setDuration(200).rotation(180);
      return true;
    } else {
      view.animate().setDuration(200).rotation(0);
      return false;
    }
  }
}
