package com.example.abm.LoginAndRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abm.Appointments.AppointmentsMainActivity;
import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

public class LogReg_LogInActivity extends BaseActivity {

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

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = email.getText().toString();
                String textPassword = password.getText().toString();
                loginUser(textEmail, textPassword);
            }
        });
    }

    private void loginUser(String email, String password) {
        super.getAuth().signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LogReg_LogInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LogReg_LogInActivity.this, AppointmentsMainActivity.class));
                finish();
            }
        }).addOnFailureListener(e -> Toast.makeText(LogReg_LogInActivity.this, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}