package com.github.bkhezry.weather.ui.fragment;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.utils.AppUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutFragment extends DialogFragment {

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_about,
        container, false);
    ButterKnife.bind(this, view);
    String versionName = "";
    try {
      versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
    } catch (PackageManager.NameNotFoundException e) {
      // do nothing
    }
    setTextWithLinks(view.findViewById(R.id.text_application_info), getString(R.string.application_info_text, versionName));
    setTextWithLinks(view.findViewById(R.id.text_developer_info), getString(R.string.developer_info_text));
    setTextWithLinks(view.findViewById(R.id.text_libraries), getString(R.string.libraries_text));
    setTextWithLinks(view.findViewById(R.id.text_license), getString(R.string.license_text));
    return view;
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
}
