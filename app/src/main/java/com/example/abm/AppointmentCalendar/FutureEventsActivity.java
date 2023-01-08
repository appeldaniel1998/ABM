package com.example.abm.AppointmentCalendar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.AppointmentType.AppointmentType;
import com.example.abm.BaseActivity;
import com.example.abm.HistoryAnalytics.ClientActivities;
import com.example.abm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

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
    public static HashMap<String, AppointmentType> appointmentTypes = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calendar_future_events);
        initMenuSideBar();

        //get the current date
        int currentDate = getCurrentDate();
        //now we will display all events that their date is bigger or equal to currentDate
        database = super.getCurrDatabase();
        database.collection("Appointment").get();
        progressDialog = ProgressDialog.show(this, "Appointments", "Loading, please wait....", true);
        auth = super.getCurrFirebaseAuth();
        recyclerView = findViewById(R.id.recyclerViewFuture);
        for (int i = 0; i < Event.eventsList.size(); i++) {
            if (currentDate <= Event.eventsList.get(i).getDate()) {
                clientFutureEvents.add(Event.eventsList.get(i));
            }
        }
        getAllAppointmentTypesAndPrices();
    }

    private void getAllAppointmentTypesAndPrices() {
        database = super.getCurrDatabase();
        database.collection("Appointment Types").get()
                //.addOnFailureListener(e-> Toast.makeText(FutureEventsActivity.this, "Fail!", Toast.LENGTH_SHORT).show());
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        AppointmentType currType = document.toObject(AppointmentType.class);
                        appointmentTypes.put(currType.getTypeName(), currType);
                    }
                    initRecyclerView(progressDialog, this, recyclerView);
                });
    }

    //function get current date as int in structure YYYYMMDD
    private int getCurrentDate() {
        Calendar calendar = Calendar.getInstance();//get the current date in Android Studio
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + 1);
        String currDate = year + month + day;//merge all to one string
        int date = Integer.parseInt(currDate);//convert the string to int
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
