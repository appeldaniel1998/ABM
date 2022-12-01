package com.example.abm.Appointments;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.abm.BaseActivity;
import com.example.abm.R;

public class AppointmentsMainActivity extends BaseActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_main);
        super.initMenuSideBar();

        //Code here!
    }
}