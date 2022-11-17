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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.abm.Appointments.AppointmentsMainActivity;
import com.example.abm.Cart.CartMainActivity;
import com.example.abm.Clients.ClientsMainActivity;
import com.example.abm.HistoryAnalytics.AnalyticsMainActivity;
import com.example.abm.Products.ProductsMainActivity;
import com.example.abm.R;
import com.google.android.material.navigation.NavigationView;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */

public class LogReg_LoginOrRegisterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button registerButton;
    private Button logInButton;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logreg_login_or_register);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        registerButton = findViewById(R.id.registerButton);
        logInButton = findViewById(R.id.logInButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogReg_LoginOrRegisterActivity.this, LogReg_RegisterActivity.class));
            }
        });
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogReg_LoginOrRegisterActivity.this, LogReg_LogInActivity.class));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();
        if (item.getItemId() == R.id.menuItemLogReg)
        {
            return true;
        }
        else if (item.getItemId() == R.id.menuItemAppointments)
        {
            startActivity(new Intent(this, AppointmentsMainActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.menuItemProducts)
        {
            startActivity(new Intent(this, ProductsMainActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.menuItemClients)
        {
            startActivity(new Intent(this, ClientsMainActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.menuItemAnalytics)
        {
            startActivity(new Intent(this, AnalyticsMainActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.menuItemCart)
        {
            startActivity(new Intent(this, CartMainActivity.class));
            return true;
        }
        else return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}