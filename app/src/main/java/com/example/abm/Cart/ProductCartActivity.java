package com.example.abm.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.AppointmentCalendar.CalendarMainActivity;
import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProductCartActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Cart> cart = new ArrayList<>();
    private CartAdapter adapter;
    private Button finishOrderButton;
    private TextView totalPrice;
    private int totalSum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_cart);
        super.initMenuSideBar();
        createCartList();
        finishOrderButton = findViewById(R.id.FinishOrder);
        totalPrice = findViewById(R.id.SetTotalPrice);


        //go all over the cart array list and add each object to "Orders" collection in the database
        finishOrderButton.setOnClickListener(v -> {
            for (Cart cart : cart) {
                super.getCurrDatabase().collection("Orders").document(super.getCurrFirebaseAuth().getCurrentUser().getUid()).collection("Cart").document(cart.getProductColor()).set(cart);
            }
            // go to cart collection in the database, get the current user's cart according to document id (uid), get the products and delete them
            Toast.makeText(this, "Finish Order ! ", Toast.LENGTH_SHORT).show();
            super.getCurrDatabase().collection("Cart").document(super.getCurrFirebaseAuth().getCurrentUser().getUid()).collection("Products").get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    documentSnapshot.getReference().delete();
                }
            });
            startActivity(new Intent(ProductCartActivity.this, CalendarMainActivity.class));
        });


    }

    private void createCartList() {
        //get all the data from the database from the current from Products collection and add it to the arraylist
        super.getCurrDatabase().collection("Cart").document(super.getCurrFirebaseAuth().getUid()).collection("Products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Cart c = documentSnapshot.toObject(Cart.class);
                    System.out.println(c);
                    totalSum += c.getPrice();
                    System.out.println("Total price: " + totalSum);
                    cart.add(c);

                }
                //init the total price of the cart
                totalPrice.setText(String.valueOf(totalSum));
                buildRecyclerView();

            }

        });

    }


    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.recycleViewCartActivity); //recycleView is the id of the recycleView in the xml file
        recyclerView.setHasFixedSize(true); //recycler view will not change in size
        layoutManager = new LinearLayoutManager(this); //layout manager is the way the items are displayed
        adapter = new CartAdapter(cart); //adapter is the way the data is displayed in the recycler view
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}