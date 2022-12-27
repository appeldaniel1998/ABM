package com.example.abm.AppointmentCalendar;

import static com.example.abm.Cart.ProductCartActivity.ordersList;
import static com.example.abm.HistoryAnalytics.HistoryActivity.clientActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.AppointmentType.AppointmentType;
import com.example.abm.BaseActivity;
import com.example.abm.Cart.Orders;
import com.example.abm.Cart.ProductCartActivity;
import com.example.abm.Clients.Client;
import com.example.abm.HistoryAnalytics.AnalyticsActivity;
import com.example.abm.HistoryAnalytics.AnalyticsDatabaseUtils;
import com.example.abm.HistoryAnalytics.ClientActivities;
import com.example.abm.HistoryAnalytics.HistoryActivity;
import com.example.abm.HistoryAnalytics.HistoryRecycleAdapter;
import com.example.abm.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FutureEventsActivity extends BaseActivity {
    private final HashMap<String, ArrayList<ClientActivities>> clientActivitiesPerYear = new HashMap<>();
    private ArrayList<String> relevantYears;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    FirebaseFirestore database;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    static ArrayList<Event> clientFutureEvents = new ArrayList<>();//Create new list of all events of current client

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calendar_future_events);
        initMenuSideBar();

//        Button historyButton = findViewById(R.id.analyticsButton); //set onclick listener to button "History"
//        historyButton.setOnClickListener(v -> startActivity(new Intent(FutureEventsActivity.this, HistoryActivity.class)));

        //get the current date
        int currentDate = getCurrentDate();
        /////////////////I need to display here all event that their dated are bigger or equal to currentDate
        database = super.getCurrDatabase();
        database.collection("Appointment").get();
        progressDialog = ProgressDialog.show(this, "Appointments", "Loading, please wait....", true);
        auth = super.getCurrFirebaseAuth();
        recyclerView = findViewById(R.id.recyclerViewFuture);
        /////////////////////////////////////////////
        clientFutureEvents.addAll(Event.eventsList);
        for (int i = 0; i < clientFutureEvents.size(); i++) {
            int dateOfEvent = clientFutureEvents.get(i).getDate();
            if (currentDate>dateOfEvent)//if this is not a future event (i.e its date supposed to be bigger then today's date)
            {
                clientFutureEvents.remove(clientFutureEvents.get(i));//remove this event from clientFutureEvents array
            }
        }
//////////////////////////////////////////////////////////////////////////////
        initRecyclerView(progressDialog,this,recyclerView);


//.addOnSuccessListener


}
    //function get current date as int in structure YYYYMMDD
    private int getCurrentDate() {
        Calendar calendar = Calendar.getInstance();//get the current date in Android Studio
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String currDate= year+month+day;//merge all to one string
        int date= Integer.parseInt(currDate);//convert the string to int
        return date;//return the number we get as the current date
    }

    public static void initRecyclerView(ProgressDialog progressDialog, Context context, RecyclerView recyclerView) {
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(context);

        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        // Sort the ArrayList in descending order by the date
        Collections.sort(clientFutureEvents, (o1, o2) -> o2.getDate() - o1.getDate());

        FutureEventsRecycleViewAdapter recyclerViewAdapter = new FutureEventsRecycleViewAdapter(clientFutureEvents);
        recyclerView.setAdapter(recyclerViewAdapter);
        progressDialog.dismiss();
    }
}
