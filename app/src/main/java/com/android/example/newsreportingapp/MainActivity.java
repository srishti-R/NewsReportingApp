package com.android.example.newsreportingapp;

import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.example.newsreportingapp.ReminderUtils.SettingPeriodicSync;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PagerTabStrip tab= findViewById(R.id.tabMode);

        ViewPager pager= findViewById(R.id.view_pager);
        pager.setAdapter(new NewsFragmentPagerAdapter(getSupportFragmentManager()));

        SettingPeriodicSync.scheduleReminder(this);


    }


}
