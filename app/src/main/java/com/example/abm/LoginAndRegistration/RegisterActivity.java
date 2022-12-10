package com.example.abm.LoginAndRegistration;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abm.Appointments.AppointmentCalendar.CalendarMainActivity;
import com.example.abm.BaseActivity;
import com.example.abm.Clients.Client;
import com.example.abm.R;
import com.example.abm.Utils.DatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends BaseActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phoneNumber;
    private EditText address;
    private EditText password;
    private EditText retypePassword;

    private TextView birthdayDate;
    private DatePickerDialog datePickerDialog;

    private Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg_register);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        password = findViewById(R.id.password);
        retypePassword = findViewById(R.id.retypePassword);
        findViewById(R.id.AppBar).setVisibility(View.GONE);


        register = findViewById(R.id.registerButton);
        birthdayDate = findViewById(R.id.birthdayDatePicker);

        // Initiating date picks handling
        // Initializes all parameters needed for date picker and sets default value of today
        datePickerDialog = DatePicker.initDatePicker(birthdayDate, this);
        birthdayDate.setText(DatePicker.getTodayDate()); // Set initial date to today's date


        //onclick listener for register button
        register.setOnClickListener(v -> {
            //Converting fields to text
            String textFirstName = firstName.getText().toString();
            String textLastName = lastName.getText().toString();
            String textEmail = email.getText().toString();
            String textPhoneNumber = phoneNumber.getText().toString();
            String textAddress = address.getText().toString();
            String textPassword = password.getText().toString();
            String textRetypePassword = retypePassword.getText().toString();
            String textBirthdayDate = birthdayDate.getText().toString();

            if (TextUtils.isEmpty(textEmail) || TextUtils.isEmpty(textPassword) || TextUtils.isEmpty(textFirstName) || TextUtils.isEmpty(textLastName) || TextUtils.isEmpty(textPhoneNumber)) {
                Toast.makeText(RegisterActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
            } else if (textPassword.length() < 6) {
                Toast.makeText(RegisterActivity.this, "The password must contain at least 6 characters!", Toast.LENGTH_SHORT).show();
            } else if (!textPassword.equals(textRetypePassword)) {
                Toast.makeText(RegisterActivity.this, "The passwords do not match!", Toast.LENGTH_SHORT).show();
            } else {
                // if all fields are correct, register the user
                registerUser(textFirstName, textLastName, textEmail, textPhoneNumber, textAddress, textPassword, DatePicker.stringToInt(textBirthdayDate), super.getCurrFirebaseAuth());
            }
        });
    }


    public void registerUser(String textFirstName, String textLastName, String textEmail, String textPhoneNumber, String textAddress, String textPassword, int textBirthdayDate, FirebaseAuth auth) {
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) { // if authenticator succeeded in creating a user
                FirebaseUser user = auth.getCurrentUser();

                assert user != null;
                String userUID = user.getUid();

                Client userToAdd = new Client(textFirstName, textLastName, textEmail, textPhoneNumber, textAddress, textBirthdayDate, userUID); //creating a new user
                super.getCurrDatabase().collection("Clients").document(userUID).set(userToAdd); //adding user data to database

                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                //upon success, move to appointments main activity
                startActivity(new Intent(RegisterActivity.this, CalendarMainActivity.class));
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Onclick listener when the layout (the line) of "birthday"  is pressed.
     */
    public void onClickBirthdayLinearLayout(View view) {
        datePickerDialog.show();
    }
}