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
    public static void getAppointmentsFromDB(int fromDate, int toDate, FirebaseFirestore database, FirebaseUser user, ProgressDialog progressDialog) {//progressDialog-show the loading symbole
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
                                database.collection("Appointments") // general appointments
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> { //move over all external docoment
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
                                                            progressDialog.dismiss();//hide the loading symbole
                                                        });
                                            }
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
                                                            progressDialog.dismiss();
                                                        });
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    /**
     * The function gets the apponintment types names from the database into an arraylist which it returns.
     *
     * @param database       Firestore database instance
     * @param progressDialog Porgress dialog to be dismissed when get is complete
     * @return new arraylist of Appointment type names
     */
    public static ArrayList<String> getAppointmentTypesFromDB(FirebaseFirestore database, ProgressDialog progressDialog) {
        ArrayList<String> appointmentTypes = new ArrayList<>();
        database.collection("Appointment Types").orderBy("typeName")
                .get()
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        String name = (String) data.get("typeName");
                        appointmentTypes.add(name);
                    }
                    progressDialog.dismiss();
                });
        return appointmentTypes;
    }

    /**
     * A function to retrieve the full names of clients from the WeekViewActivity.clients hashmap
     *
     * @param progressDialog Progress dialog to be dismissed when task is completed
     * @return The arraylist of full names of clients as strings.
     */
    public static ArrayList<String> getClientNamesFromDB(ProgressDialog progressDialog) {
        ArrayList<String> clientNames = new ArrayList<>();
        for (Client client : WeekViewActivity.clients.values()) {
            clientNames.add(client.getFirstName() + " " + client.getLastName());
        }
        progressDialog.dismiss();
        return clientNames;
    }

    /**
     * The function retrieves the clients from the Firestore databasae into the WeekViewActivity.clients hashmap
     * The method returns said hashmap
     *
     * @param database       Firestore database instance
     * @param progressDialog Progress dialog to be dismissed upon the completion of the task
     * @return the hashmap created
     */
    public static HashMap<String, Client> getClientsIfManager(FirebaseFirestore database, ProgressDialog progressDialog) {
        //this function called in WeekViewActivity
        HashMap<String, Client> clients = new HashMap<>();
        database.collection("Clients").orderBy("lastName")
                .get()
                .addOnCompleteListener(task -> {
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
                });
        return clients;
    }
}
