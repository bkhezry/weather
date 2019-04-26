package com.github.bkhezry.weather.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.bkhezry.weather.R;
import com.github.bkhezry.weather.model.fivedayweather.ListItem;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  private FastAdapter<ListItem> mFastAdapter;
  private ItemAdapter<ListItem> mItemAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    LinearLayoutManager layoutManager
        = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    recyclerView.setLayoutManager(layoutManager);
    mItemAdapter = new ItemAdapter<>();
    mFastAdapter = FastAdapter.with(mItemAdapter);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(mFastAdapter);
    generateList();
  }

  private void generateList() {
    ListItem listItem = new ListItem();
    mItemAdapter.clear();
    mItemAdapter.add(listItem);
    mItemAdapter.add(listItem);
    mItemAdapter.add(listItem);
    mItemAdapter.add(listItem);
    mItemAdapter.add(listItem);
  }
}
