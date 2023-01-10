package com.example.abm.Products;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abm.BaseActivity;
import com.example.abm.Clients.Client;
import com.example.abm.Cart.Cart;
import com.example.abm.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProductsClickcardActivity extends BaseActivity {

    Product product;
    private ImageView productImage;
    private TextView productColor;
    private Button addToCartButton;
    private Button editProductButton;
    private Button deleteProductButton;
    private Button plus;
    private Button minus;
    private TextView quantity;
    private TextView priceIs;
    private TextView priceCoins;
    private TextView setPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_clickcard);
        super.initMenuSideBar();
        ProductsClickCardDatabaseUtils.databaseGetClientOrManager(this);
        initValues();

        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Product", "Loading, please wait...", true);

        // what we get from the last activity
        Intent intent = getIntent();
        String productPositon = intent.getStringExtra("Product");
        ProductsClickCardDatabaseUtils.databaseSetSingleProduct(productPositon, progressDialog, this);


    }
    public void SetProduct(Product product){
        this.product= product;
    }

    public void SetIfManager() {
        findViewById(R.id.MinusPolish).setVisibility(View.GONE);
        findViewById(R.id.PlusPolish).setVisibility(View.GONE);
        findViewById(R.id.quantity).setVisibility(View.GONE);
        findViewById(R.id.addToCart).setVisibility(View.GONE);
        findViewById(R.id.deleteFromCart).setVisibility(View.GONE);

    }

    // if the user is client, remove any button which are not needed for the client
    public void SetIfClient() {
        findViewById(R.id.EditProduct).setVisibility(View.GONE);
        findViewById(R.id.deleteProduct).setVisibility(View.GONE);
        findViewById(R.id.deleteFromCart).setVisibility(View.GONE);

    }

    // Initialize the values of the layout, and set on click listeners for the buttons.
    private void initValues() {
        editProductButton = findViewById(R.id.EditProduct);
        addToCartButton = findViewById(R.id.addToCart);
        deleteProductButton = findViewById(R.id.deleteProduct);
        plus = findViewById(R.id.PlusPolish);
        minus = findViewById(R.id.MinusPolish);
        quantity = findViewById(R.id.quantity);
        priceIs = findViewById(R.id.priceIs);
        priceCoins = findViewById(R.id.PriceCoins);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToCart();

            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityInt = Integer.parseInt(quantity.getText().toString());
                quantityInt++;
                //update the quantity according to the user's choice
                quantity.setText(String.valueOf(quantityInt));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityInt = Integer.parseInt(quantity.getText().toString());
                if (quantityInt >= 1) {
                    quantityInt--;
                    //update the quantity according to the user's choice
                    quantity.setText(String.valueOf(quantityInt));
                }
            }
        });

        editProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsClickcardActivity.this, EditProduct.class);
               intent.putExtra("productColor", product.getColorName());
                startActivity(intent);
            }
        });

        deleteProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsClickcardActivity.super.getCurrDatabase().collection("Products").document(product.getColorName()).delete();
                Toast.makeText(ProductsClickcardActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProductsClickcardActivity.this, ProductsMainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AddToCart() {
        if (quantity.getText().toString().equals("0")) {
            Toast.makeText(this, "You Didn't Pick anything! ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProductsClickcardActivity.this, ProductsMainActivity.class));

        }
        else {
            //create a new cart item and add it to the collection in the database
            //set cart's price
            int totalPrice = Integer.parseInt(quantity.getText().toString()) * Integer.parseInt(product.getPrice());
            Cart cart = new Cart(product.getColorName(),product.getImage(), Integer.parseInt(quantity.getText().toString()),totalPrice);
            ProductsClickCardDatabaseUtils.databaseAddProductToCart(product, cart);
            //update the quantity of the product in the database
            int newQuantity = Integer.parseInt(product.getQuantity()) - Integer.parseInt(quantity.getText().toString());

            if (newQuantity <0){
                Toast.makeText(this, "This amount isn't available, Please choose again!", Toast.LENGTH_SHORT).show();
                quantity.setText("0");


            }
            else {
                String newQuantityString = String.valueOf(newQuantity);
                ProductsClickCardDatabaseUtils.databaseUpdateQuantity(product, newQuantityString);
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProductsClickcardActivity.this, ProductsMainActivity.class));
            }

        }

    }

    // init the values og new image and polish details on each click.
    public void initValuesOfLayout() {
        productImage = findViewById(R.id.Polish1);
        productColor = findViewById(R.id.PolishDetalis);
        setPrice = findViewById(R.id.SetPrice);
        productColor.setText(product.getColorName());
        productImage.setImageResource(product.getImage());
        setPrice.setText(product.getPrice());

    }
}