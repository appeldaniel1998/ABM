package com.example.abm.Products;

import android.os.Bundle;

import com.example.abm.BaseActivity;
import com.example.abm.R;

public class AddNewProduct extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_new_product);
        super.initMenuSideBar();
    }
}