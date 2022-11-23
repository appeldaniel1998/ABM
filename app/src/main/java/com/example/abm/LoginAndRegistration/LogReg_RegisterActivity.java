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

import com.example.abm.Appointments.AppointmentsMainActivity;
import com.example.abm.BaseActivity;
import com.example.abm.Clients.User;
import com.example.abm.R;
import com.google.firebase.auth.FirebaseUser;

public class LogReg_RegisterActivity extends BaseActivity {

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


        register = findViewById(R.id.registerButton);
        birthdayDate = findViewById(R.id.birthdayDatePicker);

        // Initiating date picks handling
        datePickerDialog = BirthdayDatePicker.initDatePicker(birthdayDate, this);
        birthdayDate.setText(BirthdayDatePicker.getTodayDate()); // Set initial date to today's date


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

            if (TextUtils.isEmpty(textEmail) || TextUtils.isEmpty(textPassword) || TextUtils.isEmpty(textFirstName) ||
                    TextUtils.isEmpty(textLastName) || TextUtils.isEmpty(textPhoneNumber)) {
                Toast.makeText(LogReg_RegisterActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
            } else if (textPassword.length() < 6) {
                Toast.makeText(LogReg_RegisterActivity.this, "The password must contain at least 6 characters!", Toast.LENGTH_SHORT).show();
            } else if (!textPassword.equals(textRetypePassword)) {
                Toast.makeText(LogReg_RegisterActivity.this, "The passwords do not match!", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(textFirstName, textLastName, textEmail, textPhoneNumber, textAddress, textPassword, textBirthdayDate);
            }
        });
    }


    private void registerUser(String textFirstName, String textLastName, String textEmail, String textPhoneNumber, String textAddress,
                              String textPassword, String textBirthdayDate) {
        super.getCurrFirebaseAuth().createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = super.getCurrFirebaseAuth().getCurrentUser();
                String userUID = user.getUid();

                User userToAdd = new User(textFirstName, textLastName, textEmail, textPhoneNumber, textAddress, textBirthdayDate); //creating a new user
                super.getCurrDatabase().collection("Clients").document(userUID).set(userToAdd); //adding user data to database

                Toast.makeText(LogReg_RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LogReg_RegisterActivity.this, AppointmentsMainActivity.class));
                finish();
            } else {
                Toast.makeText(LogReg_RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
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