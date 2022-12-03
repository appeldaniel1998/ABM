package com.example.abm.Appointments.AppointmentType;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AppointmentTypesActivity extends BaseActivity {

    private Button addAppointmentTypeButton;
    private RecyclerView recyclerView;
    private AppointmentTypeRecycleAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_types);
        initMenuSideBar();

        // Init progress dialog
        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Appointment Types", "Loading, please wait...", true);

        // Init view from XML file
        addAppointmentTypeButton = findViewById(R.id.addAppointmentTypeButton);
        recyclerView = findViewById(R.id.recyclerViewAppointmentTypes);

        // Init recycler view params
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewLayoutManager = new LinearLayoutManager(this);

        // set onclick listener to the "add appointment type" button
        addAppointmentTypeButton.setOnClickListener(v -> startActivity(new Intent(AppointmentTypesActivity.this, AddEditAppointmentTypeActivity.class)));

        // init arraylist to stored data received from firestore
        ArrayList<AppointmentType> appointmentTypes = new ArrayList<>();

        super.getCurrDatabase().collection("Appointment Types").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> data = document.getData();
                    String name = (String) data.get("typeName");
                    String price = (String) data.get("price");
                    String duration = (String) data.get("duration");
                    appointmentTypes.add(new AppointmentType(name, price, duration));
                }

                // Continued init of recycler view
                recyclerViewAdapter = new AppointmentTypeRecycleAdapter(appointmentTypes);
                recyclerView.setLayoutManager(recyclerViewLayoutManager);
                recyclerView.setAdapter(recyclerViewAdapter);

                progressDialog.dismiss(); // stop progress dialog

                recyclerViewAdapter.setOnItemClickListener(position -> {
                    Intent myIntent = new Intent(AppointmentTypesActivity.this, AddEditAppointmentTypeActivity.class);
                    myIntent.putExtra("appointmentTypeName", appointmentTypes.get(position).getTypeName()); //Optional parameters
                    AppointmentTypesActivity.this.startActivity(myIntent);
                });
            }
        });

    }
}