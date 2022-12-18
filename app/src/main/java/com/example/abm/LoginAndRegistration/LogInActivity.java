package com.example.abm.LoginAndRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abm.AppointmentCalendar.CalendarMainActivity;
import com.example.abm.BaseActivity;
import com.example.abm.R;

public class LogInActivity extends BaseActivity {
//Class to login (exist account)
    private EditText email;
    private EditText password;
    private Button logIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg_log_in);//display the page of email, password and login
//now we will finds a view that was identified by the id attribute from the XML layout resource
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        logIn = findViewById(R.id.logInButton);
        //when we will click on one button logIn:
        logIn.setOnClickListener(v -> {
            String textEmail = email.getText().toString();//we will get the user input of email
            String textPassword = password.getText().toString();//we will get the user input of password
            loginUser(textEmail, textPassword);//update in db the last time the user has been 'signed in' (firebase-authentication)
        });//and move to the monthly calendar page
    }

    private void loginUser(String email, String password) {
        //send to firebase auth - upon success logs in automatically
        super.getCurrFirebaseAuth().signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            Toast.makeText(LogInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

            // transfer to appointments main activity
            startActivity(new Intent(LogInActivity.this, CalendarMainActivity.class));
            finish();
        }).addOnFailureListener(e -> Toast.makeText(LogInActivity.this, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}