package com.example.abm.AppointmentCalendar;

import static com.example.abm.AppointmentCalendar.CalendarDatabaseUtils.getAppointmentsFromDB;
import static com.example.abm.AppointmentCalendar.CalendarDatabaseUtils.getClientsIfManager;
import static com.example.abm.AppointmentCalendar.CalendarUtils.daysInMonthArray;
import static com.example.abm.AppointmentCalendar.CalendarUtils.monthYearFromDate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class CalendarMainActivity extends BaseActivity implements CalendarAdapter.OnItemListener {//Activity_appointments_calender_main

    //To build monthly calender I used:
    // https://www.youtube.com/watch?v=Ba0Q-cK1fJo
    //https://github.com/codeWithCal/CalendarTutorialAndroidStudio/tree/WeeklyCalendar/app/src/main/java/codewithcal/au/calendarappexample
    //To build weekly calender I used:
    //https://www.youtube.com/watch?v=knpSbtbPz3o
    //To build daily calender I used:
    //https://www.youtube.com/watch?v=Aig99t-gNqM&t=0s

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    FirebaseFirestore database;
    FirebaseAuth auth;
    public static HashMap<String, Client> clients;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calender_main);//5.55 in the video: https://www.youtube.com/watch?v=Ba0Q-cK1fJo
        super.initMenuSideBar();

        database = super.getCurrDatabase();
        auth = super.getCurrFirebaseAuth();
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

        initWidgets();//define recycleviews
        //start to show the monthly calendar from current date
        CalendarUtils.selectedDate = LocalDate.now();//current date
        setMonthView();
        progressDialog = ProgressDialog.show(this, "Appointments", "Loading, please wait....", true);
        getAppointmentsFromDB(-1, -1, database, auth.getCurrentUser(), progressDialog); // update appointments in the Event.eventList

    }

    //define the calendar recycle view and the title (between 2 arrows)
    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView22);//id from activity_appointments_calender_main.xml
        monthYearText = findViewById(R.id.monthYearTV22);//text between 2 arrows
    }

    //define monthly viewing
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);// we're going to have 7 colunms in our recycle view
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    //by clicking on the 'back arrow' in month view this function will show us prev month
    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);//prev month
        setMonthView();
    }

    //by clicking on the 'next arrow' in month view this function will show us next month
    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);//next month
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)//7:39 : https://www.youtube.com/watch?v=knpSbtbPz3o
    {//
        if (date != null)//we will have the days before and after the month
        {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }

    }

    //when clicking on 'WEEKLY' button
    public void weeklyAction(View view) {
        startActivity(new Intent(this, WeekViewActivity.class));
    }

    public void futureEventsAction(View view) {
        startActivity(new Intent(this, FutureEventsActivity.class));
    }
}