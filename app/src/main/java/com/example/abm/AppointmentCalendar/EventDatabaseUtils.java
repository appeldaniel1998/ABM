package com.example.abm.AppointmentCalendar;

import static com.example.abm.AppointmentCalendar.CalendarDatabaseUtils.getAppointmentsFromDB;
import static com.example.abm.AppointmentCalendar.CalendarDatabaseUtils.getClientsIfManager;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.abm.Clients.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.HashMap;

public class EventDatabaseUtils {
    static FirebaseFirestore database;
    static FirebaseAuth auth;
    private static ProgressDialog progressDialog;

    public static void Retrivel(CalendarMainActivity calendarMainActivity) {
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        ProgressDialog clientsProgressDialog = ProgressDialog.show(calendarMainActivity, "Appointments", "Loading, please wait....", true);
        database.collection("Clients").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            Client currUser = documentSnapshot.toObject(Client.class);
            if (currUser != null) {
                if (currUser.getManager()) { // user is a manager
                    // get all clients from DB to represent it in drop down list
                    CalendarMainActivity.clients = getClientsIfManager(database, clientsProgressDialog);
                } else {
                    CalendarMainActivity.clients = new HashMap<>();
                    CalendarMainActivity.clients.put(currUser.getUid(), currUser);
                    clientsProgressDialog.dismiss();
                }
            }
        });
        CalendarMainActivity.initAndSetMonthView(calendarMainActivity, database, auth, progressDialog);
         }
}
