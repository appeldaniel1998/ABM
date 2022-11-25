package com.example.abm.Clients;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abm.BaseActivity;
import com.example.abm.LoginAndRegistration.BirthdayDatePicker;
import com.example.abm.R;

public class ClientsCreateClient extends BaseActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phoneNumber;
    private EditText address;
    private EditText password;
    private EditText retypePassword;

    private TextView birthdayDate;
    private DatePickerDialog datePickerDialog;

    private Button addClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg_register);
        super.initMenuSideBar();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        birthdayDate = findViewById(R.id.birthdayDatePicker);
        addClient = findViewById(R.id.registerButton);

        findViewById(R.id.password).setVisibility(View.GONE);
        findViewById(R.id.retypePassword).setVisibility(View.GONE);

        // Initiating date picks handling
        datePickerDialog = BirthdayDatePicker.initDatePicker(birthdayDate, this);
        birthdayDate.setText(BirthdayDatePicker.getTodayDate()); // Set initial date to today's date






    }
}