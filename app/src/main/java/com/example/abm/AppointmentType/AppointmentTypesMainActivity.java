package com.example.abm.AppointmentType;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.BaseActivity;
import com.example.abm.R;

import java.util.ArrayList;

public class AppointmentTypesMainActivity extends BaseActivity {
    private Button addAppointmentTypeButton;
    private RecyclerView recyclerView;
    private AppointmentTypeRecycleAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ProgressDialog progressDialog;

    public static ArrayList<AppointmentType> appointmentTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_types);
        initMenuSideBar();

        // Init progress dialog
        progressDialog = ProgressDialog.show(this, "Appointment Types", "Loading, please wait...", true);

        // Init view from XML file
        addAppointmentTypeButton = findViewById(R.id.addAppointmentTypeButton);
        recyclerView = findViewById(R.id.recyclerViewAppointmentTypes);

        // Init recycler view params
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewLayoutManager = new LinearLayoutManager(this);

        // set onclick listener to the "add appointment type" button
        addAppointmentTypeButton.setOnClickListener(v -> startActivity(new Intent(AppointmentTypesMainActivity.this, AddEditAppointmentTypeActivity.class)));

        // init arraylist to stored data received from firestore
        appointmentTypes = new ArrayList<>();

        AppointmentTypeDatabaseUtils.getAppointmentTypes(AppointmentTypesMainActivity.this); // get appointment types from database
    }

    public static void onSuccessGetFromDB(AppointmentTypesMainActivity appointmentTypesMainActivity) {
        // Continued init of recycler view
        appointmentTypesMainActivity.recyclerViewAdapter = new AppointmentTypeRecycleAdapter(appointmentTypes);
        appointmentTypesMainActivity.recyclerView.setLayoutManager(appointmentTypesMainActivity.recyclerViewLayoutManager);
        appointmentTypesMainActivity.recyclerView.setAdapter(appointmentTypesMainActivity.recyclerViewAdapter);

        appointmentTypesMainActivity.progressDialog.dismiss(); // stop progress dialog

        appointmentTypesMainActivity.recyclerViewAdapter.setOnItemClickListener(position -> {
            Intent myIntent = new Intent(appointmentTypesMainActivity, AddEditAppointmentTypeActivity.class);
            myIntent.putExtra("appointmentTypeName", appointmentTypes.get(position).getTypeName()); //Optional parameters
            appointmentTypesMainActivity.startActivity(myIntent);
        });
    }


}