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
import com.example.abm.Clients.Client;
import com.example.abm.Products.ProductsClickcardActivity;
import com.example.abm.Products.ProductsMainActivity;
import com.example.abm.R;
import com.example.abm.Utils.DatePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ProductCartActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Cart> cart = new ArrayList<>();
    private ArrayList<Orders> ordersList = new ArrayList<>();
    private CartAdapter adapter;
    private Button finishOrderButton;
    private TextView totalPrice;
    private int totalSum = 0;
    private String clientId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_cart);
        super.initMenuSideBar();
        createCartList();
        finishOrderButton = findViewById(R.id.FinishOrder);
        totalPrice = findViewById(R.id.SetTotalPrice);
        clientId = super.getCurrFirebaseAuth().getCurrentUser().getUid();


        //go all over the cart array list and add each object to "Orders" collection in the database
        finishOrderButton.setOnClickListener(v -> {
            String orderID = UUID.randomUUID().toString();
            for (Cart cart : cart) {
                //add the cart object to the database, save each cart in different document
                super.getCurrDatabase().collection("Orders").document(clientId).collection("User Orders")
                        .document(orderID).collection("Order Details").document(cart.getProductColor()).set(cart).addOnSuccessListener(aVoid -> {
                            Toast.makeText(ProductCartActivity.this, "Order has been placed successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProductCartActivity.this, CalendarMainActivity.class);
                            startActivity(intent);
                        });
            }
            //Add the time, date, price to the same order document
            LocalTime date = LocalTime.now(); //get current time. the time supposed to be with 4 digit.
            String time = date.toString();
            String CurrentTime = time.substring(0, 2) + time.substring(3, 5); //get just 4 digit from the time without the " : "

            Orders order = new Orders(clientId, String.valueOf(totalSum), DatePicker.stringToInt(DatePicker.getTodayDate()), CurrentTime);
            super.getCurrDatabase().collection("Orders").document(clientId).collection("User Orders")
                    .document(orderID).set(order);
            // go to cart collection in the database, get the current user's cart according to document id (uid), get the products and delete them
            Toast.makeText(this, "Finish Order ! ", Toast.LENGTH_SHORT).show();
            super.getCurrDatabase().collection("Cart").document(super.getCurrFirebaseAuth().getCurrentUser().getUid()).collection("Products").get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    documentSnapshot.getReference().delete();
                }
            });
            startActivity(new Intent(ProductCartActivity.this, CalendarMainActivity.class));
        });
        getOrdersFromDB();

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
        adapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                cart.get(position);
                Intent intent = new Intent(ProductCartActivity.this, ProductsClickcardActivity.class);
                intent.putExtra("Product", (CharSequence) cart.get(position).getProductColor());
                startActivity(intent);

            }

            @Override
            public void onAddClick(int position) {

            }


        });
    }

    private void getOrdersFromDB() {
        super.getCurrDatabase().collection("Clients").document(clientId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Client currUser = documentSnapshot.toObject(Client.class);
                    if (currUser != null) {
                        if (currUser.getManager()) {
                            //get all the orders from the database and add to the orders list
                            super.getCurrDatabase().collectionGroup("User Orders").get().addOnSuccessListener(queryDocumentSnapshots -> {
                                for (DocumentSnapshot documentSnapshot2 : queryDocumentSnapshots.getDocuments()) {
                                    Map<String, Object> data = documentSnapshot2.getData();
                                    //System.out.println("------------------------------------------------:::: data " + data);
                                    Orders order = new Orders(data.get("clientName").toString(), data.get("price").toString(), Integer.parseInt(data.get("date").toString()), data.get("time").toString());
                                    ordersList.add(order);
                                }
//                                for (Orders order : ordersList) {
//                                    System.out.println("------------------------------------------------:::: order list " + order.toString());
//                                }
                            });
                        } else {
                            //print the orders
                            super.getCurrDatabase().collection("Orders").document(clientId).collection("User Orders").get().addOnSuccessListener(queryDocumentSnapshots -> {
                                for (DocumentSnapshot documentSnapshot2 : queryDocumentSnapshots.getDocuments()) {
                                    Map<String, Object> data = documentSnapshot2.getData();
                                    //System.out.println("------------------------------------------------:::: data " + data);
                                    Orders order = new Orders(data.get("clientName").toString(), data.get("price").toString(), Integer.parseInt(data.get("date").toString()), data.get("time").toString());
                                    ordersList.add(order);
                                }
//                                for (Orders order : ordersList) {
//                                    System.out.println("------------------------------------------------:::: order list " + order.toString());
//                                }
                            });

                        }

                    }
                });
    }
}