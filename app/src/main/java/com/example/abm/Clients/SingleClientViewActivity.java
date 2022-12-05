package com.example.abm.Clients;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.example.abm.Utils.DatePicker;

public class SingleClientViewActivity extends BaseActivity {

    Client client;
    String clientUID;

    private TextView titleName;
    private TextView name;
    private TextView email;
    private TextView birthday;
    private TextView phoneNumber;
    private TextView address;

    private Button editClientButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_single_client_view);
        super.initMenuSideBar();

        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Client", "Loading, please wait...", true);

        Intent intent = getIntent();
        clientUID = intent.getStringExtra("clientUID");

        editClientButton = findViewById(R.id.editClientButton);
        titleName = findViewById(R.id.titleFullName);
        name = findViewById(R.id.fullNamePlaceholder);
        email = findViewById(R.id.emailPlaceholder);
        birthday = findViewById(R.id.birthdayPlaceholder);
        phoneNumber = findViewById(R.id.phoneNumberPlaceholder);
        address = findViewById(R.id.addressPlaceholder);

        super.getCurrDatabase().collection("Clients").document(clientUID).get().addOnSuccessListener(documentSnapshot -> { // If client info retrieved successfully from the DB
            client = documentSnapshot.toObject(Client.class);
            initValuesOfLayout();
            progressDialog.dismiss();

            editClientButton.setOnClickListener(v -> {
                Intent myIntent = new Intent(SingleClientViewActivity.this, EditClientActivity.class);
                myIntent.putExtra("clientUID", clientUID); //Optional parameters
                SingleClientViewActivity.this.startActivity(myIntent);
            });
        });
    }

    private void initValuesOfLayout() {
        String fullName = client.getFirstName() + " " + this.client.getLastName();
        titleName.setText(fullName);
        name.setText(fullName);
        email.setText(client.getEmail());
        birthday.setText(DatePicker.intToString(client.getBirthdayDate()));
        phoneNumber.setText(client.getPhoneNumber());
        address.setText(client.getAddress());
    }
}