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
import com.example.abm.Products.Cart.Cart;
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
        checkMangerorClient();

    }

    private void checkMangerorClient() {
            FirebaseUser user = super.getCurrFirebaseAuth().getCurrentUser();
            if (user != null) {
                String UserUid = user.getUid();
                super.getCurrDatabase().collection("Clients").document(UserUid)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            Client client = documentSnapshot.toObject(Client.class);
                            TextView name = findViewById(R.id.nameMenuHeader);
                            if (client != null) {
                                //check if the current user is a client or manager
                                if (client.getManager()) {
                                    // remove any button which are not needed for the manager
                                    findViewById(R.id.MinusPolish).setVisibility(View.GONE);
                                    findViewById(R.id.PlusPolish).setVisibility(View.GONE);
                                    findViewById(R.id.quantity).setVisibility(View.GONE);
                                    findViewById(R.id.addToCart).setVisibility(View.GONE);

                                } else {
                                    // remove any button which are not needed for the client
                                    findViewById(R.id.EditProduct).setVisibility(View.GONE);

                                }
                            }
                        });
            }

        }



    // Initialize the values of the layout, and set on click listeners for the buttons.
    private void initValues() {
        editProductButton = findViewById(R.id.EditProduct);
        addToCartButton = findViewById(R.id.addToCart);
        plus = findViewById(R.id.PlusPolish);
        minus = findViewById(R.id.MinusPolish);
        quantity = findViewById(R.id.quantity);
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

        editProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ntent intent = new Intent(ProductsClickcardActivity.this, EditProductActivity.class);
                //intent.putExtra("Product", product.getId());
                //startActivity(intent);
            }
        });
    }

    private void AddToCart() {
        if (quantity.getText().toString().equals("0")) {
            Toast.makeText(this, "You Didn't Pick anything", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProductsClickcardActivity.this, ProductsMainActivity.class));

        }
        else {
            //create a new cart item and add it to the collection in the database
            Cart cart = new Cart(product.getColorName(),product.getImage(), Integer.parseInt(quantity.getText().toString()));
            super.getCurrDatabase().collection("Cart").document(super.getCurrFirebaseAuth().getCurrentUser().getUid()).collection("Products").document(product.getColorName()).set(cart);
            //update the quantity of the product in the database
            int newQuantity = Integer.parseInt(product.getQuantity()) - Integer.parseInt(quantity.getText().toString());
            String newQuantityString = String.valueOf(newQuantity);
            super.getCurrDatabase().collection("Products").document(product.getColorName()).update("quantity", newQuantityString);
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProductsClickcardActivity.this, ProductsMainActivity.class));

        }

    }

    // init the values og new image and polish details on each click.
    private void initValuesOfLayout() {
        productImage = findViewById(R.id.Polish1);
        productColor = findViewById(R.id.PolishDetalis);
        productColor.setText(product.getColorName());
        productImage.setImageResource(product.getImage());

    }
}