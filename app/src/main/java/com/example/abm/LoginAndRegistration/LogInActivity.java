package com.example.abm.LoginAndRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abm.Appointments.AppointmentCalendar.CalendarMainActivity;
import com.example.abm.BaseActivity;
import com.example.abm.R;

public class LogInActivity extends BaseActivity {

    private EditText email;
    private EditText password;
    private Button logIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg_log_in);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        logIn = findViewById(R.id.logInButton);

        logIn.setOnClickListener(v -> {
            String textEmail = email.getText().toString();
            String textPassword = password.getText().toString();
            loginUser(textEmail, textPassword);
        });
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