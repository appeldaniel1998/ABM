package com.example.abm.Products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_cart);
        super.initMenuSideBar();
        createCartList();
        //ProgressDialog progressDialog;
        //progressDialog = ProgressDialog.show(this, "Cart", "Loading, please wait...", true);
        }

    private void createCartList() {
        //get all the data from the database from the current from Products collection and add it to the arraylist

        super.getCurrDatabase().collection("Cart").document(super.getCurrFirebaseAuth().getUid()).collection("Products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Cart c = documentSnapshot.toObject(Cart.class);
//                    System.out.println("-------------------------------------");
//                    System.out.println(c);
                    cart.add(c);
                }
//                System.out.println("-------------------------------------");
//                for (Cart c : cart) {
//                    System.out.println(c);
//                }
//                System.out.println("-------------------------------------");
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