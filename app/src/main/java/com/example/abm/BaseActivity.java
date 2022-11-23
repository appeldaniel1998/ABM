package com.example.abm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.abm.Appointments.AppointmentsMainActivity;
import com.example.abm.Cart.CartMainActivity;
import com.example.abm.Clients.ClientsMainActivity;
import com.example.abm.HistoryAnalytics.AnalyticsMainActivity;
import com.example.abm.Products.ProductsMainActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    public FirebaseAuth getCurrFirebaseAuth() {
        return auth;
    }
    public FirebaseFirestore getCurrDatabase() {
        return database;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.auth = FirebaseAuth.getInstance();
        this.database = FirebaseFirestore.getInstance();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();
        if (item.getItemId() == R.id.menuItemLogReg) {
            return true;
        } else if (item.getItemId() == R.id.menuItemAppointments) {
            startActivity(new Intent(this, AppointmentsMainActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menuItemProducts) {
            startActivity(new Intent(this, ProductsMainActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menuItemClients) {
            startActivity(new Intent(this, ClientsMainActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menuItemAnalytics) {
            startActivity(new Intent(this, AnalyticsMainActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menuItemCart) {
            startActivity(new Intent(this, CartMainActivity.class));
            return true;
        } else return false;
    }


    public void initMenuSideBar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
}