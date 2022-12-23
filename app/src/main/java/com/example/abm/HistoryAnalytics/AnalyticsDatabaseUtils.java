package com.example.abm.HistoryAnalytics;

import static com.example.abm.HistoryAnalytics.AnalyticsMainActivity.clientActivities;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.AppointmentCalendar.Event;
import com.example.abm.AppointmentType.AppointmentType;
import com.example.abm.Clients.Client;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsDatabaseUtils {

    /**
     * The function to retrieve appointments from the Firestore database and put them into the Event.eventsList arraylist.
     * The function does not return but update the arraylist.
     *
     * @param fromDate       Do which date to take appointments (-1 for all)
     * @param toDate         Do which date to take appointments (-1 for all)
     * @param database       Firestore database instance
     * @param user           Firebase authentication user
     * @param progressDialog progress dialog to be dismissed when task is finished
     */
    public static void getAppointmentsFromDB(int fromDate, int toDate, FirebaseFirestore database, FirebaseUser user, ProgressDialog progressDialog,
                                             Context context, RecyclerView recyclerView) {//progressDialog-show the loading symbole
        Event.eventsList = new ArrayList<>();
        if (fromDate == -1) fromDate = Integer.MIN_VALUE;
        if (toDate == -1) toDate = Integer.MAX_VALUE;

        int finalFromDate = fromDate;
        int finalToDate = toDate;

        if (user != null) //if we seccuees to get from DB
        {
            // getting data and checking if user is manager or client
            String UserUid = user.getUid();
            database.collection("Clients").document(UserUid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Client currUser = documentSnapshot.toObject(Client.class);
                        if (currUser != null) {
                            if (currUser.getManager()) { // user is a manager
                                //display all events for all clients

                                // get all the documents from the Appointments collection and from each document get the appointments collection
                                database.collectionGroup("Client Appointments")
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            for (QueryDocumentSnapshot documentSnapshot2 : queryDocumentSnapshots) {
                                                Map<String, Object> data = documentSnapshot2.getData();
                                                String clientName = (String) data.get("clientName");
                                                String clientID = (String) data.get("clientId");
                                                String typeApp = (String) data.get("appointmentType");
                                                String idApp = (String) data.get("appointmentId");
                                                String startTime = (String) data.get("startTime");
                                                int date = Integer.parseInt(data.get("date") + "");

                                                Event appointment = new Event(idApp, typeApp, clientName, date, startTime, clientID);
                                                if (appointment.getDate() > finalFromDate && appointment.getDate() < finalToDate) {
                                                    Event.eventsList.add(appointment);
                                                }
                                                Event.eventsList.add(appointment);
                                            }
                                            getAppointmentTypesFromDB(database, progressDialog, context, recyclerView);

                                        });
                            } else { // user is a client
                                //display current event for specific client
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
                                                            getAppointmentTypesFromDB(database, progressDialog, context, recyclerView);
                                                        });
                                            }
                                        });
                            }
                        }
                    });
//            try {
//                Tasks.await(database.collection("Clients").document(UserUid).get());
//            } catch (ExecutionException | InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }


    /**
     * The function gets the apponintment types from the database into an arraylist which it returns.
     *
     * @param database       Firestore database instance
     * @param progressDialog Porgress dialog to be dismissed when get is complete
     * @return new arraylist of Appointment types
     */
    public static void getAppointmentTypesFromDB(FirebaseFirestore database, ProgressDialog progressDialog,
                                                                             Context context, RecyclerView recyclerView) {
        AnalyticsMainActivity.appointmentTypes = new HashMap<>();
//        ArrayList<AppointmentType> appointmentTypes = new ArrayList<>();
        database.collection("Appointment Types").orderBy("typeName")
                .get()
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        AppointmentType currType = document.toObject(AppointmentType.class);
                        AnalyticsMainActivity.appointmentTypes.put(currType.getTypeName(), currType);
                    }
                    progressDialog.dismiss();
                    RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(context);

                    recyclerView.hasFixedSize();
                    recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
                    recyclerView.setLayoutManager(recyclerViewLayoutManager);

                    clientActivities = new ArrayList<>();
                    clientActivities.addAll(Event.eventsList);

                    HistoryRecycleAdapter recyclerViewAdapter = new HistoryRecycleAdapter(clientActivities);
                    recyclerView.setAdapter(recyclerViewAdapter);
                });
    }
}
