package com.example.abm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.abm.Appointments.AppointmentsMainActivity;
import com.example.abm.Cart.CartMainActivity;
import com.example.abm.Clients.Client;
import com.example.abm.Clients.ClientsMainActivity;
import com.example.abm.HistoryAnalytics.AnalyticsMainActivity;
import com.example.abm.LoginAndRegistration.LogReg_LoginOrRegisterActivity;
import com.example.abm.Products.ProductsMainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
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
        } else if (item.getItemId() == R.id.menuItemSignOut) {
            this.auth.signOut();
            if (this.auth.getCurrentUser() == null) {
                Toast.makeText(this, "User Signed Out!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "User Signed Out Failed!", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(this, LogReg_LoginOrRegisterActivity.class));
            return true;
        } else return false;
    }


    public void initMenuSideBar() {
        Toolbar toolbar = findViewById(R.id.ProductsRecycleView); //TODO generalize - for the sake of code correctness, pass as an argument to function, functionally no difference if all names are the same
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        FirebaseUser user = this.auth.getCurrentUser();
        if (user != null) {
            String UserUid = user.getUid();
            database.collection("Clients").document(UserUid)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Client client = documentSnapshot.toObject(Client.class);
                            TextView name = findViewById(R.id.nameMenuHeader);

                            assert client != null;
                            String fullName = client.getFirstName() + " " + client.getLastName();
                            name.setText(fullName);

                            TextView email = findViewById(R.id.emailMenuHeader);
                            email.setText(client.getEmail());

                            TextView isManager = findViewById(R.id.isManagerMenuHeader);
                            if (client.isManager()) {
                                isManager.setText("Manager");
                            }
                            else {
                                isManager.setText("Client");
                            }
                        }
                    });
        }
    }
}