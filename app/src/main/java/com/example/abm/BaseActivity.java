package com.example.abm;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.abm.AppointmentCalendar.CalendarMainActivity;
import com.example.abm.AppointmentType.AppointmentTypesActivity;
import com.example.abm.Cart.ProductCartActivity;
import com.example.abm.Clients.Client;
import com.example.abm.Clients.ClientsMainActivity;
import com.example.abm.HistoryAnalytics.HistoryActivity;
import com.example.abm.LoginAndRegistration.LoginOrRegisterActivity;
import com.example.abm.Products.ProductsMainActivity;
import com.example.abm.Utils.BackendHandling;
import com.example.abm.Utils.CallbackInterface;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private StorageReference storageReference;
    private String url = "http://192.168.1.246:5000";
    private RequestBody requestBody;
    private String postBodyString;
    private MediaType mediaType;
    protected final Object lock = new Object();
//    private String POST = "POST";
//    private String GET="GET";


    public final int IMG_REQUEST_CODE_GALLERY = 10;

    public FirebaseAuth getCurrFirebaseAuth() {
        return auth;
    }

    public FirebaseFirestore getCurrDatabase() {
        return database;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.auth = FirebaseAuth.getInstance();
        this.database = FirebaseFirestore.getInstance();
        this.storageReference = FirebaseStorage.getInstance().getReference();
    }

    private RequestBody buildRequestBody(String msg) {
        postBodyString = msg;
        mediaType = MediaType.parse("text/plain");
        requestBody = RequestBody.create(postBodyString, mediaType);
        return requestBody;
    }

//    public void postRequest(String message, CallbackInterface callBack) {
//        RequestBody requestBody = buildRequestBody(message);
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request.Builder().post(requestBody).url(url).build();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(final Call call, final IOException e) {
//                runOnUiThread(() -> {
//                    Toast.makeText(BaseActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    call.cancel();
//                });
//            }
//            @Override
//            public void onResponse(Call call, final Response response) {
//                runOnUiThread(() -> {
//                    boolean success = false;
//                    while (!success) {
//                        try {
//                            String jsonString = response.body().string();
//                            JSONArray jsonArray = new JSONArray(jsonString);
//                            BackendHandling.handleServerResponses(jsonArray);
//                            callBack.onCall();
//                            success = true;
//                        } catch (Exception e) {
//                        }
//                    }
//                });
//            }
//        });
//    }

    public void postRequest(String message, CallbackInterface callBack) {
        new Thread(() ->
        {
            // Used to set custom name to the current thread
            Thread.currentThread().setName("myThread");
            try {
                URL newUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) newUrl.openConnection();
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(message.getBytes(StandardCharsets.UTF_8));

                TimeUnit.SECONDS.sleep(5);

                InputStream inputStream = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                reader.close();

                // print the response
                JSONArray jsonArray = new JSONArray(response.toString());
                BackendHandling.handleServerResponses(jsonArray);
                callBack.onCall();

//                outputStream.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }


    //On click listener: what to do when each button is clicked
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers(); // close nav drawer
        if (item.getItemId() == R.id.menuItemAppointments) {
            startActivity(new Intent(this, CalendarMainActivity.class));
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
            startActivity(new Intent(this, HistoryActivity.class));
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

            database.collection("Clients").document(userUid).get().addOnSuccessListener(documentSnapshot -> {
                Client currUser = documentSnapshot.toObject(Client.class);

                ImageView profilePicNavBar = findViewById(R.id.profileImageMenuHeader);
                StorageReference profilePicReference = storageReference.child("Clients").child(userUid).child("profile.jpg");
                //Connecting with Firebase storage and retrieving image
                profilePicReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(BaseActivity.this).load(uri).into(profilePicNavBar);
                });

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