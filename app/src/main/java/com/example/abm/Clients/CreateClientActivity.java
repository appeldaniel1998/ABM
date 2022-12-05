package com.example.abm.Clients;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abm.BaseActivity;
import com.example.abm.Utils.DatePicker;
import com.example.abm.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateClientActivity extends BaseActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phoneNumber;
    private EditText address;
    private TextView birthdayDate;
    private TextView title;

    private DatePickerDialog datePickerDialog;

    private FirebaseFirestore database;

    private Button addClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg_register);
        super.initMenuSideBar();

        database = super.getCurrDatabase();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        birthdayDate = findViewById(R.id.birthdayDatePicker);
        addClient = findViewById(R.id.registerButton);
        title = findViewById(R.id.title);

        findViewById(R.id.password).setVisibility(View.GONE); //remove password inputs from the layout.
        findViewById(R.id.retypePassword).setVisibility(View.GONE);

        // Initiating date picks handling
        datePickerDialog = DatePicker.initDatePicker(birthdayDate, this);
        birthdayDate.setText(DatePicker.getTodayDate()); // Set initial date to today's date

        addClient.setOnClickListener(v -> {
            //Converting fields to text
            String textFirstName = firstName.getText().toString();
            String textLastName = lastName.getText().toString();
            String textEmail = email.getText().toString();
            String textPhoneNumber = phoneNumber.getText().toString();
            String textAddress = address.getText().toString();
            int textBirthdayDate = DatePicker.stringToInt(birthdayDate.getText().toString());

            if (TextUtils.isEmpty(textFirstName) || TextUtils.isEmpty(textEmail)) {
                Toast.makeText(CreateClientActivity.this, "Empty email or first name!", Toast.LENGTH_SHORT).show();
            } else {
                String uid = String.valueOf(java.util.UUID.randomUUID()); //Create a random UID for the new client
                Client userToAdd = new Client(textFirstName, textLastName, textEmail, textPhoneNumber, textAddress, textBirthdayDate, uid); //creating a new user
                database.collection("Clients").document(uid).set(userToAdd); //adding user data to database
                Toast.makeText(CreateClientActivity.this, "Client added successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateClientActivity.this, ClientsMainActivity.class));
                finish();
            }
        });

    }

    /**
     * Onclick  listener when the layout (the line) of "birthday"  is pressed.
     */
    public void onClickBirthdayLinearLayout(View view) {
        datePickerDialog.show();
    }
}