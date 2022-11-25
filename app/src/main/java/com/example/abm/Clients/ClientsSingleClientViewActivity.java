package com.example.abm.Clients;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class ClientsSingleClientViewActivity extends BaseActivity {

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
        setContentView(R.layout.activity_clients_single_client_view);
        super.initMenuSideBar();

        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Client", "Loading, please wait...", true);

        Intent intent = getIntent();
        clientUID = intent.getStringExtra("clientUID");

        super.getCurrDatabase().collection("Clients").document(clientUID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        client = documentSnapshot.toObject(Client.class);
                        initValuesOfLayout();
                        progressDialog.dismiss();
                    }
                });


    }

    private void initValuesOfLayout() {
        titleName = findViewById(R.id.titleFullName);
        name = findViewById(R.id.fullNamePlaceholder);
        email = findViewById(R.id.emailPlaceholder);
        birthday = findViewById(R.id.birthdayPlaceholder);
        phoneNumber = findViewById(R.id.phoneNumberPlaceholder);
        address = findViewById(R.id.addressPlaceholder);

        String fullName = client.getFirstName() + " " + this.client.getLastName();
        titleName.setText(fullName);
        name.setText(fullName);
        email.setText(client.getEmail());
        birthday.setText(client.getBirthdayDate());
        phoneNumber.setText(client.getPhoneNumber());
        address.setText(client.getAddress());
    }
}