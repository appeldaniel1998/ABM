package com.example.abm.Products;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProductsClickcardActivity extends BaseActivity {

    Product product;
    private ImageView productImage;
    private TextView productColor;
    private Button addToCartButton;
    private Button plus;
    private Button minus;
    private TextView quantity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_clickcard);
        super.initMenuSideBar();
        initValues();



        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Product", "Loading, please wait...", true);

        Intent intent = getIntent();
        String productPositon = intent.getStringExtra("Product");

        super.getCurrDatabase().collection("Products").document(productPositon)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        product = documentSnapshot.toObject(Product.class);
                        initValuesOfLayout();
                        progressDialog.dismiss();
                    }
                });


    }

    // Initialize the values of the layout, and set on click listeners for the buttons.
    private void initValues() {

        addToCartButton = findViewById(R.id.addToCart);
        plus = findViewById(R.id.PlusPolish);
        minus = findViewById(R.id.MinusPolish);
        quantity = findViewById(R.id.quantity);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsClickcardActivity.this, ProductsMainActivity.class));
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityInt = Integer.parseInt(quantity.getText().toString());
                quantityInt++;
                quantity.setText(String.valueOf(quantityInt));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityInt = Integer.parseInt(quantity.getText().toString());
                if (quantityInt >= 1) {
                    quantityInt--;
                    quantity.setText(String.valueOf(quantityInt));
                }
            }
        });
    }

    // init the values og new image and polish details on each click.
    private void initValuesOfLayout() {
        productImage = findViewById(R.id.Polish1);
        productColor = findViewById(R.id.PolishDetalis);
        productColor.setText(product.getColor_name());
        productImage.setImageResource(product.getImage());

    }
}