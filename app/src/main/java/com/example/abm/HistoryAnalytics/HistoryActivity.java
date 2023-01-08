package com.example.abm.HistoryAnalytics;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.AppointmentType.AppointmentType;
import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.example.abm.Utils.DatePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
Activity to present the history of all purchases of products/past appointments of clients or manager + sum total
*/
public class HistoryActivity extends BaseActivity {

    public static HashMap<String, AppointmentType> appointmentTypes;
    public static ArrayList<ClientActivities> clientActivities;

    FirebaseFirestore database;
    FirebaseAuth auth;

    private TextView totalRevenueTextView;
    private Button analyticsButton;

    private RecyclerView recyclerView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_history);
        super.initMenuSideBar();

        database = super.getCurrDatabase();
        auth = super.getCurrFirebaseAuth();

        analyticsButton = findViewById(R.id.analyticsButton);
        analyticsButton.setOnClickListener(v -> {
            startActivity(new Intent(HistoryActivity.this, AnalyticsActivity.class));
        });

        database.collection("Clients").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                boolean isManager = (boolean) data.get("manager");
                if (!isManager) {
                    analyticsButton.setVisibility(View.GONE);
                }
            }
        });

        totalRevenueTextView = findViewById(R.id.totalRevenue);
        recyclerView = findViewById(R.id.recyclerViewHistory);

        //Get current date as int for comparison
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateString = DatePicker.makeDateString(day, month, year);
        int todayDate = DatePicker.stringToInt(dateString);

        ProgressDialog analyticsProgressDialog = ProgressDialog.show(this, "Analytics", "Loading, please wait....", true);
        AnalyticsDatabaseUtils.getAppointmentsFromDB(-1, -1, database, auth.getCurrentUser(), analyticsProgressDialog, this, recyclerView, totalRevenueTextView);

        //get orders from appropriate function.
        //clientActivities.addAll(orders);
    }

    public static void initRecyclerView(ProgressDialog progressDialog, Context context, RecyclerView recyclerView, TextView totalRevenueTextView) {
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(context);

        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        double totalRevenue = 0;
        for (int i = 0; i < clientActivities.size(); i++) {
            totalRevenue += Math.round(Double.parseDouble(clientActivities.get(i).getPrice()) * 100) / 100.0;
        }
        totalRevenueTextView.setText(totalRevenue + "");

        // Sort the ArrayList in descending order by the date
        Collections.sort(clientActivities, (o1, o2) -> o2.getDate() - o1.getDate());

        HistoryRecycleAdapter recyclerViewAdapter = new HistoryRecycleAdapter(clientActivities);
        recyclerView.setAdapter(recyclerViewAdapter);
        progressDialog.dismiss();
    }
}

/*
 * For clients:
 * make recycler view of appointments
 * make recycler view of orders
 *
 * For managers:
 * make recycler view of appointments with sum total
 * make recycler view of orders with sum total
 * make list per year per month revenues
 */