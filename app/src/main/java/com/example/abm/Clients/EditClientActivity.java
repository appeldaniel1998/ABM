package com.example.abm.Clients;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditClientActivity extends BaseActivity {

    Client client;
    String clientUID;

    private TextView titleName;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView birthday;
    private TextView phoneNumber;
    private TextView address;

    private Button doneEditingButton;

    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg_register);

        database = super.getCurrDatabase();

        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Edit Client", "Loading, please wait...", true);

        doneEditingButton = findViewById(R.id.registerButton);
        titleName = findViewById(R.id.title);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        birthday = findViewById(R.id.birthdayDatePicker);
        findViewById(R.id.password).setVisibility(View.GONE);
        findViewById(R.id.retypePassword).setVisibility(View.GONE);

        Intent intent = getIntent();
        clientUID = intent.getStringExtra("clientUID");

        database.collection("Clients").document(clientUID)
                .get()
                .addOnSuccessListener(documentSnapshot -> { // If client info retrieved successfully from the DB
                    client = documentSnapshot.toObject(Client.class);
                    initValuesOfLayout();
                    progressDialog.dismiss();

                    doneEditingButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Client userToAdd = new Client(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(),
                                    phoneNumber.getText().toString(), address.getText().toString(), birthday.getText().toString(), clientUID); //creating a new user
                            database.collection("Clients").document(clientUID).set(userToAdd); //adding user data to database

                            Intent myIntent = new Intent(EditClientActivity.this, SingleClientViewActivity.class);
                            myIntent.putExtra("clientUID", clientUID); //Optional parameters
                            EditClientActivity.this.startActivity(myIntent);
                            finish();
                        }
                    });
                });
    }

    @SuppressLint("SetTextI18n")
    private void initValuesOfLayout() {
        String fullName = client.getFirstName() + " " + this.client.getLastName();
        titleName.setText(fullName);
        firstName.setText(client.getFirstName());
        lastName.setText(client.getLastName());
        email.setText(client.getEmail());
        birthday.setText(client.getBirthdayDate());
        phoneNumber.setText(client.getPhoneNumber());
        address.setText(client.getAddress());
        doneEditingButton.setText("Done");
    }
}