package com.example.abm.Products;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.abm.BaseActivity;
import com.example.abm.R;

public class ProductsClickcardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_clickcard);
        super.initMenuSideBar();
    }
}