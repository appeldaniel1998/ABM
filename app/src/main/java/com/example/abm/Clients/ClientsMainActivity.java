package com.example.abm.Clients;

import android.os.Bundle;

import com.example.abm.ProductsActivity;
import com.example.abm.R;

public class ClientsMainActivity extends ProductsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_main);
        super.initMenuSideBar();

        //Code here!
    }
}