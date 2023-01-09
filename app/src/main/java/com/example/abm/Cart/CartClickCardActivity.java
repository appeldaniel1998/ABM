package com.example.abm.Cart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abm.BaseActivity;
import com.example.abm.Products.Product;
import com.example.abm.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class CartClickCardActivity extends BaseActivity {

    Product product;
    private ImageView productImage;
    private TextView productColor;
    private TextView setPrice;
    private TextView quantity;
    private Button addToCartButton;
    private Button plus;
    private Button minus;
    private Button deleteFromCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_clickcard);
        super.initMenuSideBar();
        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(this, "Product", "Loading, please wait...", true);

        // what we get from the last activity
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
        deleteFromCart = findViewById(R.id.deleteFromCart);
        deleteFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }

    private void deleteProduct() {
        super.getCurrDatabase().collection("Cart").document(super.getCurrFirebaseAuth().getCurrentUser().getUid()).
                collection("Products").document(product.getColorName()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CartClickCardActivity.this, "Product deleted from cart", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CartClickCardActivity.this, CartMainActivity.class);
                        startActivity(intent);
                    }
                });

    }

    private void initValuesOfLayout() {
        addToCartButton = findViewById(R.id.addToCart);
        productImage = findViewById(R.id.Polish1);
        productColor = findViewById(R.id.PolishDetalis);
        setPrice = findViewById(R.id.SetPrice);
        quantity = findViewById(R.id.quantity);
        super.getCurrDatabase().collection("Cart").document(super.getCurrFirebaseAuth().getCurrentUser().getUid()).
                collection("Products").document(product.getColorName()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Cart cart = documentSnapshot.toObject(Cart.class);
                        String q = String.valueOf(cart.getQuantity());
                        quantity.setText(q);
                    }
                });
        productColor.setText(product.getColorName());
        productImage.setImageResource(product.getImage());
        setPrice.setText(product.getPrice());
        plus = findViewById(R.id.PlusPolish);
        minus = findViewById(R.id.MinusPolish);
        findViewById(R.id.EditProduct).setVisibility(View.GONE);
        findViewById(R.id.deleteProduct).setVisibility(View.GONE);
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
    }

    private void AddToCart() {

            if (quantity.getText().toString().equals("0")) {
                Toast.makeText(this, "You Didn't Pick anything! ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CartClickCardActivity.this, CartMainActivity.class));

            }
            else {
                //create a new cart item and add it to the collection in the database
                //set cart's price
                int totalPrice = Integer.parseInt(quantity.getText().toString()) * Integer.parseInt(product.getPrice());
                Cart cart = new Cart(product.getColorName(),product.getImage(), Integer.parseInt(quantity.getText().toString()),totalPrice);
                super.getCurrDatabase().collection("Cart").document(super.getCurrFirebaseAuth().getCurrentUser().getUid()).collection("Products").document(product.getColorName()).set(cart);
                //update the quantity of the product in the database
                int newQuantity = Integer.parseInt(product.getQuantity()) - Integer.parseInt(quantity.getText().toString());

                if (newQuantity <0){
                    Toast.makeText(this, "This amount isn't available, Please choose again!", Toast.LENGTH_SHORT).show();
                    quantity.setText("0");


                }
                else {
                    String newQuantityString = String.valueOf(newQuantity);
                    super.getCurrDatabase().collection("Products").document(product.getColorName()).update("quantity", newQuantityString);
                    Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CartClickCardActivity.this, CartMainActivity.class));
                }

            }


    }
}