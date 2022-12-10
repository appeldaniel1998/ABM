/**
 * Copyright 2021 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.abm.LoginAndRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.abm.Appointments.AppointmentCalendar.CalendarMainActivity;
import com.example.abm.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */

public class LoginOrRegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) { // if user is logged in
            // if user logged in, go to appointments (no login necessary)
            Toast.makeText(this, "User logged in!!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginOrRegisterActivity.this, CalendarMainActivity.class));
            finish();
        } else {
            // Otherwise, start and run the activity normally
            setContentView(R.layout.activity_logreg_login_or_register);

            registerButton = findViewById(R.id.registerButton);
            logInButton = findViewById(R.id.logInButton);

            registerButton.setOnClickListener(v -> startActivity(new Intent(LoginOrRegisterActivity.this, RegisterActivity.class)));
            logInButton.setOnClickListener(v -> startActivity(new Intent(LoginOrRegisterActivity.this, LogInActivity.class)));
        }
    }
}