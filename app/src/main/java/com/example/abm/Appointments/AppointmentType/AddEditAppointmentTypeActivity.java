package com.example.abm.Appointments.AppointmentType;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddEditAppointmentTypeActivity extends BaseActivity {

    private String currAppointmentTypeName;

    private EditText newAppointmentType;
    private EditText newAppointmentPrice;
    private EditText newAppointmentDuration;

    private Button addAppointmentType;

    private FirebaseFirestore database;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_add_edit_appointment_type);
        initMenuSideBar();

        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Edit Client", "Loading, please wait...", true);

        database = super.getCurrDatabase();

        newAppointmentType = findViewById(R.id.newAppointmentTypeName);
        newAppointmentPrice = findViewById(R.id.newAppointmentPrice);
        newAppointmentDuration = findViewById(R.id.newAppointmentDuration);
        addAppointmentType = findViewById(R.id.addAppointmentTypeButton);

        Intent intent = getIntent();
        currAppointmentTypeName = intent.getStringExtra("appointmentTypeName"); // if editing, the appointment type name is passed
        if (currAppointmentTypeName == null) { // if adding and not editing, the name is not passed, hence set to null
            currAppointmentTypeName = "";
            progressDialog.dismiss();
        }
        else { //if editing
            database.collection("Appointment Types").document(currAppointmentTypeName)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        AppointmentType appointmentType = documentSnapshot.toObject(AppointmentType.class);
                        if (appointmentType != null) {
                            newAppointmentType.setText(appointmentType.getTypeName());
                            newAppointmentPrice.setText(appointmentType.getPrice());
                            newAppointmentDuration.setText(appointmentType.getDuration());
                            addAppointmentType.setText("Done");
                            TextView title = findViewById(R.id.title);
                            title.setText("Edit: " + appointmentType.getTypeName());
                        }
                        progressDialog.dismiss();
                    });
        }

        // Add appointment Button Onclick
        addAppointmentType.setOnClickListener(v -> {
            database.collection("Appointment Types").document(currAppointmentTypeName)
                    .delete()
                    .addOnSuccessListener(unused -> {
                        addNewItem();
                    });
        });
    }

    private void addNewItem() {
        String newAppointmentTypeTxt = newAppointmentType.getText().toString();
        String newAppointmentPriceTxt = newAppointmentPrice.getText().toString();
        String newAppointmentDurationTxt = newAppointmentDuration.getText().toString();

        if (TextUtils.isEmpty(newAppointmentDurationTxt) || TextUtils.isEmpty(newAppointmentPriceTxt) || TextUtils.isEmpty(newAppointmentTypeTxt)) {
            Toast.makeText(AddEditAppointmentTypeActivity.this, "Empty fields!", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                double price = Double.parseDouble(newAppointmentPriceTxt);
                double duration = Double.parseDouble(newAppointmentDurationTxt);
            }
            catch (NumberFormatException nfe) {
                Toast.makeText(AddEditAppointmentTypeActivity.this, "Price or Duration fields contain non-numbers", Toast.LENGTH_SHORT).show();
            }
            AppointmentType appointmentType = new AppointmentType(newAppointmentTypeTxt, newAppointmentPriceTxt, newAppointmentDurationTxt);
            database.collection("Appointment Types").document(newAppointmentTypeTxt).set(appointmentType); //adding user data to database
            Toast.makeText(AddEditAppointmentTypeActivity.this, "Appointment Type added successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddEditAppointmentTypeActivity.this, AppointmentTypesActivity.class));
            finish();
        }
    }
}