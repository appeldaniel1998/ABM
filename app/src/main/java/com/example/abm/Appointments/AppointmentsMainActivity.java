package com.example.abm.Appointments;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.abm.BaseActivity;
import com.example.abm.R;

import java.util.ArrayList;

public class AppointmentsMainActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private ArrayList<Appointment> appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_main);
        super.initMenuSideBar();

//        if (super.getCurrUser().getManager()) {
//
//        }
//        else {
//
//        }
    }
}