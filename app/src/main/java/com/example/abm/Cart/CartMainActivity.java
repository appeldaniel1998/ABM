package com.example.abm.Cart;

import static com.example.abm.HistoryAnalytics.HistoryActivity.clientActivities;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.abm.HistoryAnalytics.ClientActivities;
import com.example.abm.HistoryAnalytics.HistoryActivity;
import com.example.abm.R;
import com.example.abm.Utils.DatePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class CartMainActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Cart> cart = new ArrayList<>();
    public static ArrayList<ClientActivities> ordersList = new ArrayList<>();
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
        CartDatabaseUtils.databaseGetCart(this);
        finishOrderButton = findViewById(R.id.FinishOrder);
        totalPrice = findViewById(R.id.SetTotalPrice);
        clientId = super.getCurrFirebaseAuth().getCurrentUser().getUid();


        //go all over the cart array list and add each object to "Orders" collection in the database
        finishOrderButton.setOnClickListener(v -> {
            String orderID = UUID.randomUUID().toString();
            for (Cart cart : cart) {
                CartDatabaseUtils.databaseFinishOrder(cart, clientId, orderID, this);
            }
            //Add the time, date, price to the same order document
            LocalTime date = LocalTime.now(); //get current time. the time supposed to be with 4 digit.
            String time = date.toString();
            String CurrentTime = time.substring(0, 2) + time.substring(3, 5); //get just 4 digit from the time without the " : "
            Order order = new Order(clientId, String.valueOf(totalSum), DatePicker.stringToInt(DatePicker.getTodayDate()), CurrentTime);
            CartDatabaseUtils.addOrder(orderID, order, clientId);
            Toast.makeText(this, "Finish Order ! ", Toast.LENGTH_SHORT).show();
            // go to cart collection in the database, get the current user's cart according to document id (uid), get the products and delete them
            CartDatabaseUtils.deleteCart();
            startActivity(new Intent(CartMainActivity.this, CalendarMainActivity.class));
        });
    }

    public void SetCart(ArrayList<Cart> cart){
        this.cart = cart;
    }

    public void SetTotalPrice(int totalSum){
        this.totalSum = totalSum;
        totalPrice.setText(String.valueOf(totalSum));
    }


    public void buildRecyclerView() {
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
                Intent intent = new Intent(CartMainActivity.this, CartClickCardActivity.class);
                intent.putExtra("Product", (CharSequence) cart.get(position).getProductColor());
                startActivity(intent);
            }

            @Override
            public void onAddClick(int position) {

            }
        });
    }

    public static void getOrdersFromDB(String clientId, ProgressDialog progressDialog, Context context, RecyclerView recyclerView, TextView totalRevenueTextView) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Clients").document(clientId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Client currUser = documentSnapshot.toObject(Client.class);
                    if (currUser != null) {
                        if (currUser.getManager()) {
                            //get all the orders from the database and add to the orders list
                            database.collectionGroup("User Orders").get().addOnSuccessListener(queryDocumentSnapshots -> {
                                for (DocumentSnapshot documentSnapshot2 : queryDocumentSnapshots.getDocuments()) {
                                    Map<String, Object> data = documentSnapshot2.getData();
                                    Order order = new Order(data.get("clientName").toString(), data.get("price").toString(), Integer.parseInt(data.get("date").toString()), data.get("time").toString());
                                    ordersList.add(order);
                                }
                                clientActivities.addAll(ordersList);
                                HistoryActivity.initRecyclerView(progressDialog, context, recyclerView, totalRevenueTextView);
                            });
                        } else {
                            //print the orders
                            database.collection("Orders").document(clientId).collection("User Orders").get().addOnSuccessListener(queryDocumentSnapshots -> {
                                for (DocumentSnapshot documentSnapshot2 : queryDocumentSnapshots.getDocuments()) {
                                    Map<String, Object> data = documentSnapshot2.getData();
                                    Order order = new Order(data.get("clientName").toString(), data.get("price").toString(), Integer.parseInt(data.get("date").toString()), data.get("time").toString());
                                    ordersList.add(order);
                                }
                                clientActivities.addAll(ordersList);
                                HistoryActivity.initRecyclerView(progressDialog, context, recyclerView, totalRevenueTextView);
                            });
                        }
                    }
                });
    }
}