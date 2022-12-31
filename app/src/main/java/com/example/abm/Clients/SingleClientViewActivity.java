package com.example.abm.Clients;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.example.abm.Utils.DatePicker;
import com.google.firebase.storage.StorageReference;

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
        progressDialog = ProgressDialog.show(this, "Client", "Loading, please wait....", true);

        // get client uid from past clients main activity
        Intent intent = getIntent();
        clientUID = intent.getStringExtra("clientUID");

        editClientButton = findViewById(R.id.editClientButton);
        titleName = findViewById(R.id.titleFullName);
        name = findViewById(R.id.fullNamePlaceholder);
        email = findViewById(R.id.emailPlaceholder);
        birthday = findViewById(R.id.birthdayPlaceholder);
        phoneNumber = findViewById(R.id.phoneNumberPlaceholder);
        address = findViewById(R.id.addressPlaceholder);

        databaseGetClient(progressDialog);
    }

    private void databaseGetClient(ProgressDialog progressDialog) {
        super.getCurrDatabase().collection("Clients").document(clientUID).get().addOnSuccessListener(documentSnapshot -> {
            // If client info retrieved successfully from the DB
            client = documentSnapshot.toObject(Client.class);
            onClientGetOnSuccess(progressDialog);
        });
    }

    private void onClientGetOnSuccess(ProgressDialog progressDialog) {
        initValuesOfLayout();
        progressDialog.dismiss();

        ImageView profilePic = findViewById(R.id.personIcon);
        StorageReference profilePicReference = super.getStorageReference().child("Clients").child(client.getUid()).child("profile.jpg");
        //Connecting with Firebase storage and retrieving image
        profilePicReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(SingleClientViewActivity.this).load(uri).into(profilePic);
        });

        // onclick of "edit client"
        editClientButton.setOnClickListener(v -> {
            Intent myIntent = new Intent(SingleClientViewActivity.this, EditClientActivity.class);
            myIntent.putExtra("clientUID", clientUID); //Optional parameters
            SingleClientViewActivity.this.startActivity(myIntent);
        });
    }


    // display the real data of client taken from database
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