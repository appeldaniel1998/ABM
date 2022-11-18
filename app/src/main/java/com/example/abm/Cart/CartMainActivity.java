package com.example.abm.Cart;

import android.os.Bundle;

import com.example.abm.ProductsActivity;
import com.example.abm.R;

public class CartMainActivity extends ProductsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_main);
        super.initMenuSideBar();

        //Code here!
    }
}