package com.example.abm.Appointments.AppointmentCalendar;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;

import com.example.abm.Clients.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static ArrayList<String> getAppointmentTypesFromDB(FirebaseFirestore database, ProgressDialog progressDialog) {
        ArrayList<String> appointmentTypes = new ArrayList<>();
        database.collection("Appointment Types").orderBy("typeName")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            String name = (String) data.get("typeName");
                            appointmentTypes.add(name);
                        }
                        progressDialog.dismiss();
                    }
                });
        return appointmentTypes;
    }

    public static ArrayList<String> getClientNamesFromDB(FirebaseFirestore database, ProgressDialog progressDialog) {
        ArrayList<String> clientNames = new ArrayList<>();
        database.collection("Clients").orderBy("lastName")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            String firstName = (String) data.get("firstName");
                            String lastName = (String) data.get("lastName");
                            clientNames.add(firstName + " " + lastName);
                        }
                        progressDialog.dismiss();
                    }
                });
        return clientNames;
    }

    public static HashMap<String, Client> getClientsIfManager (FirebaseFirestore database, ProgressDialog progressDialog) {
        HashMap<String, Client> clients = new HashMap<>();
        database.collection("Clients").orderBy("lastName")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            String firstName = (String) data.get("firstName");
                            String lastName = (String) data.get("lastName");
                            String email = (String) data.get("email");
                            boolean isManager = (Boolean) data.get("manager");
                            String phoneNumber = (String) data.get("phoneNumber");
                            String clientUid = (String) data.get("uid");
                            int birthday = Integer.parseInt(data.get("birthdayDate") + "");
                            String address = (String) data.get("address");
                            Client currClient = new Client(firstName, lastName, email, phoneNumber, address, birthday, clientUid, isManager);
                            clients.put(clientUid, currClient);
                        }
                        progressDialog.dismiss();
                    }
                });
        return clients;
    }
}
