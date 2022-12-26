package com.example.abm.AppointmentCalendar;

import static com.example.abm.AppointmentCalendar.CalendarDatabaseUtils.getAppointmentsFromDB;
import static com.example.abm.AppointmentCalendar.CalendarUtils.daysInWeekArray;
import static com.example.abm.AppointmentCalendar.CalendarUtils.monthYearFromDate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Map;

public class WeekViewActivity<listView> extends BaseActivity implements CalendarAdapter.OnItemListener {
    //Activity_appointments_calender_week_view
    //A lot of the functions here are adapted from Calendar Main Activity
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private FirebaseFirestore database;
    private FirebaseAuth auth;


    //function get client name and return his ID
    public static String getKey(String clientName) {
        for (Map.Entry<String, Client> entry : CalendarMainActivity.clients.entrySet()) {
            String tempName = entry.getValue().getFirstName() + " " + entry.getValue().getLastName();
            if (tempName.equals(clientName)) {
                return entry.getValue().getUid();
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calender_week_view);
        super.initMenuSideBar();

        initWidgets();
        setWeekView();

        database = super.getCurrDatabase();
        auth = super.getCurrFirebaseAuth();
        //access data from DB
//try to understand if this is manager or client

        ListView listView = findViewById(R.id.eventListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event item = (Event) parent.getItemAtPosition(position);
                Intent intent = new Intent(WeekViewActivity.this, EventAddActivity.class);
                String appIDFromItem = item.getAppointmentId();
                intent.putExtra("appointmentID", appIDFromItem);
                startActivity(intent);
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
        Intent intent = new Intent(WeekViewActivity.this, EventAddActivity.class);
        intent.putExtra("appointmentID", "-1");
        startActivity(intent);
    }

}