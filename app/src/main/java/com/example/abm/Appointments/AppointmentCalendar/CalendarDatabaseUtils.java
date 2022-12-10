package com.example.abm.Appointments.AppointmentCalendar;

import android.app.ProgressDialog;

import com.example.abm.Clients.Client;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CalendarDatabaseUtils {

    //function gets data from database into the appointments arraylist
    public static void getAppointmentsFromDB(int fromDate, int toDate, FirebaseFirestore database, FirebaseUser user, ProgressDialog progressDialog) {
        if (fromDate == -1) fromDate = Integer.MIN_VALUE;
        if (toDate == -1) toDate = Integer.MAX_VALUE;

        int finalFromDate = fromDate;
        int finalToDate = toDate;

        if (user != null) {
            // getting data and checking if user is manager or client
            String UserUid = user.getUid();
            database.collection("Clients").document(UserUid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Client currUser = documentSnapshot.toObject(Client.class);
                        if (currUser != null) {
                            if (currUser.getManager()) { // user is a manager
                                // get all the documents from the Appointments collection and from each document get the appointments collection
                                database.collection("Appointments") // general appointments
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                                                CollectionReference appointmentsCollection = documentSnapshot1.getReference().collection("Client Appointments"); // single clients appointments
                                                appointmentsCollection
                                                        .get()
                                                        .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                                            for (QueryDocumentSnapshot documentSnapshot2 : queryDocumentSnapshots1) { // for each appointment
                                                                Event appointment = documentSnapshot2.toObject(Event.class);
                                                                if (appointment.getDate() > finalFromDate && appointment.getDate() < finalToDate) { // if appointment is in range of dates
                                                                    Event.eventsList.add(appointment);
                                                                }
                                                            }
                                                            progressDialog.dismiss();
                                                        });
                                            }
                                        });
                            } else { // user is a client
                                database.collection("Appointments")
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                                                CollectionReference appointmentsCollection = documentSnapshot1.getReference().collection("Client Appointments");
                                                appointmentsCollection.get()
                                                        .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                                            for (QueryDocumentSnapshot documentSnapshot2 : queryDocumentSnapshots1) {
                                                                Event appointment = documentSnapshot2.toObject(Event.class);
                                                                if (appointment.getClientId().equals(UserUid) && appointment.getDate() > finalFromDate && appointment.getDate() < finalToDate) {
                                                                    Event.eventsList.add(appointment);
                                                                }
                                                            }
                                                            progressDialog.dismiss();
                                                        });
                                            }
                                        });
                            }
                        }
                    });
        }
    }
}
