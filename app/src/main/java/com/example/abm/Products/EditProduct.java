package com.example.abm.Products;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProduct extends BaseActivity {
    private Button btnSaveProduct;
    private TextView txtProductColor;
    private TextView txtProductPrice;
    private TextView txtProductQuantity;
    private TextView txtProductDescription;
    private int productImage;

    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        super.initMenuSideBar();
        database = super.getCurrDatabase();
        initValues();

        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Edit Product", "Loading, please wait...", true);
        //get the current product from the data base,set the values of the text views to the current product values
        super.getCurrDatabase().collection("Products").document(getIntent().getStringExtra("productColor")).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Product product = task.getResult().toObject(Product.class);
                txtProductColor.setText(product.getColorName());
                txtProductPrice.setText(product.getPrice());
                txtProductQuantity.setText(product.getQuantity());
                txtProductDescription.setText(product.getDescription());
                productImage = product.getImage();
                progressDialog.dismiss();
            }
        });
        //when the user clicks on the save button, update the product in the database
        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String color = txtProductColor.getText().toString();
                String price = txtProductPrice.getText().toString();
                String quantity = txtProductQuantity.getText().toString();
                String description = txtProductDescription.getText().toString();
                Product product = new Product(color, description, productImage, price, quantity);
                EditProduct.super.getCurrDatabase().collection("Products").document(product.getColorName()).set(product);
                Toast.makeText(EditProduct.this, "Finished editing!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditProduct.this, ProductsMainActivity.class));
                finish();
            }
        });
    }

    private void initValues() {
        btnSaveProduct = findViewById(R.id.SaveProductChanges);
        txtProductColor = findViewById(R.id.ProductColor);
        txtProductPrice = findViewById(R.id.ProductPrice);
        txtProductQuantity = findViewById(R.id.ProductQuantity);
        txtProductDescription = findViewById(R.id.ProductDescription);

    }
}