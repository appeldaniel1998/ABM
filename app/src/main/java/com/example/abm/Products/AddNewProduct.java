package com.example.abm.Products;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.firebase.auth.FirebaseUser;

public class AddNewProduct extends BaseActivity {

    private Button btnAddProduct;
    private TextView txtProductColor;
    private TextView txtProductPrice;
    private TextView txtProductQuantity;
    private TextView txtProductDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_new_product);
        super.initMenuSideBar();
        initValues();
    }

    private void initValues() {
        btnAddProduct = findViewById(R.id.addProductButton);
        txtProductColor = findViewById(R.id.ProductColor);
        txtProductPrice = findViewById(R.id.ProductPrice);
        txtProductQuantity = findViewById(R.id.ProductQuantity);
        txtProductDescription = findViewById(R.id.ProductDescription);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String color = txtProductColor.getText().toString();
                String price = txtProductPrice.getText().toString();
                String quantity = txtProductQuantity.getText().toString();
                String description = txtProductDescription.getText().toString();
                Product product = new Product(color, price, quantity, description);
                AddNewProduct.super.getCurrDatabase().collection("Products").document(product.getColorName()).set(product);
                Toast.makeText(AddNewProduct.this, "Adding new product!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddNewProduct.this, ProductsMainActivity.class));
            }
        });


}
}