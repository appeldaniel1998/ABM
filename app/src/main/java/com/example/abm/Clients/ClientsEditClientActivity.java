package com.example.abm.Clients;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.abm.R;

public class ClientsEditClientActivity extends AppCompatActivity {

    Client client;
    String clientUID;

    private TextView titleName;
    private TextView name;
    private TextView email;
    private TextView birthday;
    private TextView phoneNumber;
    private TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg_register);

        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Client", "Loading, please wait...", true);

        Intent intent = getIntent();
        clientUID = intent.getStringExtra("clientUID");

//        titleName = findViewById(R.id.titleFullName);
//        name = findViewById(R.id.fullNamePlaceholder);
//        email = findViewById(R.id.emailPlaceholder);
//        birthday = findViewById(R.id.birthdayPlaceholder);
//        phoneNumber = findViewById(R.id.phoneNumberPlaceholder);
//        address = findViewById(R.id.addressPlaceholder);
    }
}