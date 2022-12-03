package com.example.abm.Appointments.AppointmentType;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddAppointmentTypeActivity extends BaseActivity {

    private EditText newAppointmentType;
    private EditText newAppointmentPrice;
    private EditText newAppointmentDuration;

    private Button addAppointmentType;

    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_add_appointment_type);
        initMenuSideBar();
        database = super.getCurrDatabase();

        newAppointmentType = findViewById(R.id.newAppointmentTypeName);
        newAppointmentPrice = findViewById(R.id.newAppointmentPrice);
        newAppointmentDuration = findViewById(R.id.newAppointmentDuration);
        addAppointmentType = findViewById(R.id.addAppointmentTypeButton);

        addAppointmentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAppointmentTypeTxt = newAppointmentType.getText().toString();
                String newAppointmentPriceTxt = newAppointmentPrice.getText().toString();
                String newAppointmentDurationTxt = newAppointmentDuration.getText().toString();

                if (TextUtils.isEmpty(newAppointmentDurationTxt) || TextUtils.isEmpty(newAppointmentPriceTxt) || TextUtils.isEmpty(newAppointmentTypeTxt)) {
                    Toast.makeText(AddAppointmentTypeActivity.this, "Empty fields!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        double price = Double.parseDouble(newAppointmentPriceTxt);
                        double duration = Double.parseDouble(newAppointmentDurationTxt);
                    }
                    catch (NumberFormatException nfe) {
                        Toast.makeText(AddAppointmentTypeActivity.this, "Price or Duration fields contain non-numbers", Toast.LENGTH_SHORT).show();
                    }
                    AppointmentType appointmentType = new AppointmentType(newAppointmentTypeTxt, newAppointmentPriceTxt, newAppointmentDurationTxt);
                    database.collection("Appointment Types").document(newAppointmentTypeTxt).set(appointmentType); //adding user data to database
                    Toast.makeText(AddAppointmentTypeActivity.this, "Appointment Type added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddAppointmentTypeActivity.this, AppointmentTypesActivity.class));
                    finish();
                }
            }
        });
    }
}