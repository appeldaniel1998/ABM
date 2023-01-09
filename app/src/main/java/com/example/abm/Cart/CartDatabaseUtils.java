package com.example.abm.Cart;

import android.content.Intent;
import android.widget.Toast;

import com.example.abm.AppointmentCalendar.CalendarMainActivity;
import com.example.abm.Products.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartDatabaseUtils {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

    public static void databaseGetCart(CartMainActivity cartMainActivity) {
        ArrayList<Cart> cart = new ArrayList<>();
     database.collection("Cart").document(auth.getUid()).collection("Products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int totalSum = 0;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Cart c = documentSnapshot.toObject(Cart.class);
                    totalSum += c.getPrice();
                    cart.add(c);

                }
                cartMainActivity.SetCart(cart);
                cartMainActivity.SetTotalPrice(totalSum);
                cartMainActivity.buildRecyclerView();

            }

        });
    }

    public static void databaseFinishOrder(Cart cart,String clientId, String orderID, CartMainActivity cartMainActivity) {
        database.collection("Orders").document(clientId).collection("User Orders")
                .document(orderID).collection("Order Details").document(cart.getProductColor()).set(cart).addOnSuccessListener(aVoid -> {
                    Toast.makeText(cartMainActivity, "Order has been placed successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(cartMainActivity, CalendarMainActivity.class);
                    cartMainActivity.startActivity(intent);

                });
    }

    // This function is add the Order to the database
    public static void addOrder(String orderID, Order order, String clientId) {
        database.collection("Orders").document(clientId).collection("User Orders")
                .document(orderID).set(order);

    }

    //this function is used to delete all the products from the cart after the order is placed
    public static void deleteCart() {
        database.collection("Cart").document(auth.getCurrentUser().getUid()).collection("Products").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                documentSnapshot.getReference().delete();
            }
        });

    }
}
