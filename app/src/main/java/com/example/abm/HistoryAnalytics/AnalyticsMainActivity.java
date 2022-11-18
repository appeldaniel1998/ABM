package com.example.abm.HistoryAnalytics;

import android.os.Bundle;

import com.example.abm.ProductsActivity;
import com.example.abm.R;

public class AnalyticsMainActivity extends ProductsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_main);
        super.initMenuSideBar();

        //Code here!
    }
}