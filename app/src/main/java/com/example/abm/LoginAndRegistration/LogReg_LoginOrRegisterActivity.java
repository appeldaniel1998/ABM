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

import com.example.abm.BaseActivity;
import com.example.abm.R;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */

public class LogReg_LoginOrRegisterActivity extends BaseActivity {

    private Button registerButton;
    private Button logInButton;
//    private boolean isCheckedForAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg_login_or_register);
        super.initMenuSideBar();
//        this.isCheckedForAuth = false;

        registerButton = findViewById(R.id.registerButton);
        logInButton = findViewById(R.id.logInButton);

        registerButton.setOnClickListener(v -> startActivity(new Intent(LogReg_LoginOrRegisterActivity.this, LogReg_RegisterActivity.class)));
        logInButton.setOnClickListener(v -> startActivity(new Intent(LogReg_LoginOrRegisterActivity.this, LogReg_LogInActivity.class)));
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        // if user logged in, go to appointments (no login necessary)
//        if (super.getAuth().getCurrentUser() != null && !this.isCheckedForAuth) {
//            this.isCheckedForAuth = true;
//            Toast.makeText(this, "User logged in!!", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(LogReg_LoginOrRegisterActivity.this, AppointmentsMainActivity.class));
//            finish();
//        }
//    }
}