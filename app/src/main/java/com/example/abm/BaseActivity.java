package com.example.abm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.abm.Appointments.AppointmentType.AppointmentTypesActivity;
import com.example.abm.Appointments.AppointmentsMainActivity;
import com.example.abm.Clients.Client;
import com.example.abm.Clients.ClientsMainActivity;
import com.example.abm.HistoryAnalytics.AnalyticsMainActivity;
import com.example.abm.LoginAndRegistration.LoginOrRegisterActivity;
import com.example.abm.Products.Cart.ProductCartActivity;
import com.example.abm.Products.ProductsMainActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    //On click listener: what to do when each button is clicked
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers(); // close nav drawer
        if (item.getItemId() == R.id.menuItemAppointments) {
            startActivity(new Intent(this, AppointmentsMainActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menuItemAppointmentTypes) {
            startActivity(new Intent(this, AppointmentTypesActivity.class));
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
            startActivity(new Intent(this, ProductCartActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menuItemSignOut) {
            this.auth.signOut();
            if (this.auth.getCurrentUser() == null) {
                Toast.makeText(this, "User Signed Out!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "User Signed Out Failed !", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(this, LoginOrRegisterActivity.class));
            return true;
        } else return false;
    }

    // init side menu in all activities TODO
    @SuppressLint("SetTextI18n")
    public void initMenuSideBar() {
        Toolbar toolbar = findViewById(R.id.ProductsRecycleView); // init toolbar
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        Menu menu = navigationView.getMenu();

        FirebaseUser user = this.auth.getCurrentUser();
        if (user != null) {
            String userUid = user.getUid();
            database.collection("Clients").document(userUid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Client currUser = documentSnapshot.toObject(Client.class);
                        TextView name = findViewById(R.id.nameMenuHeader);
                        if (currUser != null) {
                            //Toggle visibility for menu items in accordance to whether the user is a client or a manager
                            if (currUser.getManager()) { // A manager
                                // remove any page which a client can get no access to
                                MenuItem cart = menu.findItem(R.id.menuItemCart);
                                cart.setVisible(false);

                            } else { // A client
                                // remove any page which a manager can get no access to
                                MenuItem clients = menu.findItem(R.id.menuItemClients);
                                clients.setVisible(false);

                                MenuItem appointmentTypes = menu.findItem(R.id.menuItemAppointmentTypes);
                                appointmentTypes.setVisible(false);

                                menu.findItem(R.id.menuItemAnalytics).setTitle("History");
                            }


                            // Set name and email in the menu screen header of each page
                            String fullName = currUser.getFirstName() + " " + currUser.getLastName();
                            name.setText(fullName);

                            TextView email = findViewById(R.id.emailMenuHeader);
                            email.setText(currUser.getEmail());

                            TextView isManager = findViewById(R.id.isManagerMenuHeader);
                            System.out.println("Is Manager: " + currUser.getManager());
                            if (currUser.getManager()) {
                                isManager.setText("Manager");
                            } else {
                                isManager.setText("Client");
                            }
                        }
                    });
        }
    }

}