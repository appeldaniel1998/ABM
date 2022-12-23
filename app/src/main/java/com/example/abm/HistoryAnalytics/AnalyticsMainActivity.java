package com.example.abm.HistoryAnalytics;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.AppointmentCalendar.Event;
import com.example.abm.AppointmentType.AppointmentType;
import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/*
Activity to present the history of all purchases of products/past appointments of clients or manager + sum total
*/
public class AnalyticsMainActivity extends BaseActivity {

    public static HashMap<String, AppointmentType> appointmentTypes;
    public static ArrayList<Event> appointments;
    public static ArrayList<ClientActivities> clientActivities;

    FirebaseFirestore database;
    FirebaseAuth auth;

    private RecyclerView recyclerView;
//    private HistoryRecycleAdapter recyclerViewAdapter;
//    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_main);
        super.initMenuSideBar();

        database = super.getCurrDatabase();
        auth = super.getCurrFirebaseAuth();

        recyclerView = findViewById(R.id.recyclerViewHistory);

        ProgressDialog analyticsProgressDialog = ProgressDialog.show(this, "Analytics", "Loading, please wait....", true);
        AnalyticsDatabaseUtils.getAppointmentsFromDB(-1, -1, database, auth.getCurrentUser(), analyticsProgressDialog, this, recyclerView);
        appointments = Event.eventsList;

        //get orders from appropriate function.
        //clientActivities.addAll(orders);


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