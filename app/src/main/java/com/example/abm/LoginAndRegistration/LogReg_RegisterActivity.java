package com.example.abm.LoginAndRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.abm.Appointments.AppointmentsMainActivity;
import com.example.abm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogReg_RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button register;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.registerButton);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = email.getText().toString();
                String textPassword = password.getText().toString();

                if (TextUtils.isEmpty(textEmail) || TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(LogReg_RegisterActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else if (textPassword.length() < 6) {
                    Toast.makeText(LogReg_RegisterActivity.this, "The password must contain at least 6 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(textEmail, textPassword);
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LogReg_RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LogReg_RegisterActivity.this, "Registration was successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LogReg_RegisterActivity.this, AppointmentsMainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LogReg_RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}