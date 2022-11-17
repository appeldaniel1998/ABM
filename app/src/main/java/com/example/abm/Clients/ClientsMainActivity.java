package com.example.abm.Clients;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.abm.Appointments.AppointmentsMainActivity;
import com.example.abm.Cart.CartMainActivity;
import com.example.abm.HistoryAnalytics.AnalyticsMainActivity;
import com.example.abm.LoginAndRegistration.LogReg_LoginOrRegisterActivity;
import com.example.abm.Products.ProductsMainActivity;
import com.example.abm.R;
import com.google.android.material.navigation.NavigationView;

public class ClientsMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Code here!
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();
        if (item.getItemId() == R.id.menuItemLogReg) {
            startActivity(new Intent(this, LogReg_LoginOrRegisterActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menuItemAppointments) {
            startActivity(new Intent(this, AppointmentsMainActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menuItemProducts) {
            startActivity(new Intent(this, ProductsMainActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menuItemClients) {
            return true;
        } else if (item.getItemId() == R.id.menuItemAnalytics) {
            startActivity(new Intent(this, AnalyticsMainActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menuItemCart) {
            startActivity(new Intent(this, CartMainActivity.class));
            return true;
        } else return false;
    }
}