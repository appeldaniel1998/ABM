package com.example.abm.Cart;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;

import com.example.abm.Products.Product;
import com.example.abm.Products.ProductsClickcardActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CartClickCartDatabaseUtils {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

    public static void databaseSetSingleProduct(String productPositon, ProgressDialog progressDialog, CartClickCardActivity cartClickCardActivity) {

        database.collection("Products").document(productPositon)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Product product = new Product();
                        product = documentSnapshot.toObject(Product.class);
                        cartClickCardActivity.SetProduct(product);
                        cartClickCardActivity.initValuesOfLayout();
                        progressDialog.dismiss();
                    }
                });
    }


    public static void databaseDeleteProductFromCart(Product product, CartClickCardActivity cartClickCardActivity) {
        database.collection("Cart").document(auth.getCurrentUser().getUid()).
                collection("Products").document(product.getColorName()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(cartClickCardActivity, "Product deleted from cart", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(cartClickCardActivity, CartMainActivity.class);
                        cartClickCardActivity.startActivity(intent);
                    }
                });


    }

    public static void databaseSetQuantity(Product product,CartClickCardActivity cartClickCardActivity ){
        database.collection("Cart").document(auth.getCurrentUser().getUid()).
                collection("Products").document(product.getColorName()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Cart cart = documentSnapshot.toObject(Cart.class);
                        String q = String.valueOf(cart.getQuantity());
                        cartClickCardActivity.SetQuantity(q);
                    }
                });

    }


}
