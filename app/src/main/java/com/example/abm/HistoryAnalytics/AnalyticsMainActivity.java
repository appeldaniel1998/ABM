package com.example.abm.HistoryAnalytics;

import android.os.Bundle;

import com.example.abm.BaseActivity;
import com.example.abm.R;

public class AnalyticsMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_main);
        super.initMenuSideBar();

        //Code here!
    }
}

/*
 * For clients:
 * make recycler view of appointments
 * make recycler view of orders
 *
 * For managers:
 * make recycler view of appointments with sum total
 * make recycler view of orders with sum total
 * make list per year per month revenues
 */