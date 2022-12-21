package com.example.abm.AppointmentCalendar;

import static com.example.abm.AppointmentCalendar.CalendarDatabaseUtils.getAppointmentsFromDB;
import static com.example.abm.AppointmentCalendar.CalendarDatabaseUtils.getClientsIfManager;
import static com.example.abm.AppointmentCalendar.CalendarUtils.daysInWeekArray;
import static com.example.abm.AppointmentCalendar.CalendarUtils.monthYearFromDate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.BaseActivity;
import com.example.abm.Clients.Client;
import com.example.abm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WeekViewActivity extends BaseActivity implements CalendarAdapter.OnItemListener {
    //Activity_appointments_calender_week_view
    //A lot of the functions here are adapted from Calendar Main Activity
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private ProgressDialog progressDialog;
    private FirebaseFirestore database;
    private FirebaseAuth auth;
    public static HashMap<String, Client> clients;

    //function get client name and return his ID
    static String getkey(String ClientName)
    {
        String tempName="";
        String ID = "";
        for (Map.Entry<String, Client> entry : clients.entrySet()) {
            tempName=entry.getValue().getFirstName()+" "+entry.getValue().getLastName();
            if (tempName.equals(ClientName)) {
                //System.out.println(entry.getKey());
                ID=entry.getValue().getUid();
            }
        }
        return ID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calender_week_view);
        super.initMenuSideBar();

        initWidgets();
        setWeekView();

        progressDialog = ProgressDialog.show(this, "Appointments", "Loading, please wait....", true);
        database = super.getCurrDatabase();
        auth = super.getCurrFirebaseAuth();
        //access data from DB
        getAppointmentsFromDB(-1, -1, database, auth.getCurrentUser(), progressDialog); // update appointments in the Event.eventList
//try to understand if this is manager or client
        ProgressDialog clientsProgressDialog = ProgressDialog.show(this, "Appointments", "Loading, please wait....", true);
        database.collection("Clients").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            Client currUser = documentSnapshot.toObject(Client.class);
            if (currUser != null) {
                if (currUser.getManager()) { // user is a manager
                    // get all clients from DB to represent it in drop down list
                    clients = getClientsIfManager(database, clientsProgressDialog);
                } else {
                    clients = new HashMap<>();
                    clients.put(currUser.getUid(), currUser);
                    clientsProgressDialog.dismiss();
                }
            }
        });
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    private void setWeekView()//Same as setMonthView
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);// we're going to have 7 colunms in our recycle view
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }


    public void previousWeekAction(View view) { //Same as previous month action
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view)//Same as previousWeekAction just different in plusWeeks
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();

    }

    private void setEventAdapter() {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, EventEditActivity.class));
    }
}