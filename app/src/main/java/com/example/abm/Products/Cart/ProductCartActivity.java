package com.example.abm.Products.Cart;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.abm.Appointments.AppointmentType.AppointmentTypesActivity;
import com.example.abm.Appointments.AppointmentsMainActivity;
import com.example.abm.BaseActivity;
import com.example.abm.Products.Cart.Cart;
import com.example.abm.Products.Cart.CartAdapter;
import com.example.abm.Products.ProductsClickcardActivity;
import com.example.abm.Products.ProductsMainActivity;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_cart);
        super.initMenuSideBar();
        createCartList();
        finishOrderButton = findViewById(R.id.FinishOrder);
        // go to cart collection in the database, get the current user's cart according to document id (uid), get the products and delete them
        finishOrderButton.setOnClickListener(v -> {
            super.getCurrDatabase().collection("Cart").document(super.getCurrFirebaseAuth().getCurrentUser().getUid()).collection("Products").get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    documentSnapshot.getReference().delete();
                }
                //back to appointments activity
                Toast.makeText(this, "Finish Order ! ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProductCartActivity.this, AppointmentsMainActivity.class));

            });
        });


    }

    private void createCartList() {
        //get all the data from the database from the current from Products collection and add it to the arraylist
        super.getCurrDatabase().collection("Cart").document(super.getCurrFirebaseAuth().getUid()).collection("Products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Cart c = documentSnapshot.toObject(Cart.class);
                    cart.add(c);
                }
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