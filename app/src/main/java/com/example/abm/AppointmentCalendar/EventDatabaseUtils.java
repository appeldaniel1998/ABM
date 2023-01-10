package com.example.abm.AppointmentCalendar;

import static com.example.abm.AppointmentCalendar.CalendarDatabaseUtils.getClientsIfManager;

import android.app.ProgressDialog;

import com.example.abm.Clients.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventDatabaseUtils {
    static FirebaseFirestore database;
    static FirebaseAuth auth;

    public static void Retrival(CalendarMainActivity calendarMainActivity,ProgressDialog progressDialog) {
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
        CalendarMainActivity.initAndSetMonthView(calendarMainActivity, database, auth, progressDialog);}
    public static void getAppType(EventAddActivity eventAddActivity,ProgressDialog progressDialogAppointmentTypes,ProgressDialog progressDialogClientNames )
    {
             EventAddActivity.appointmentTypes = CalendarDatabaseUtils.getAppointmentTypesFromDB(database, progressDialogAppointmentTypes); // get appointment types from database to present in the dropdown list
             EventAddActivity.clientNames = CalendarDatabaseUtils.getClientNamesFromDB(progressDialogClientNames); // get client names to present in the dropdown list

             ProgressDialog progressDialog = ProgressDialog.show(eventAddActivity, "Add Appointment", "Loading, please wait....", true);
             //auth = super.getCurrFirebaseAuth();
             database.collection("Clients").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
                 Client currUser = documentSnapshot.toObject(Client.class);
                 if (!currUser.getManager()) { // user is a client
                     eventAddActivity.findView();
                 }
                 progressDialog.dismiss();
             });
    }

    public static DocumentReference docRefFunc(String idClientOfAppointment)
    {
        DocumentReference docRef=database.collection("Appointments").document(idClientOfAppointment);
        return docRef;
    }

    public static boolean saveData()
    {
        AtomicBoolean isManager = new AtomicBoolean(false);
        database.collection("Clients").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            Map<String, Object> data = documentSnapshot.getData();
            isManager.set((boolean) data.get("manager"));
        });
        return isManager.get();
    }

    public static void databaseAppointmentsRetrieval (Event newEvent, EventAddActivity eventAddActivity)
    {
        database.collection("Appointments")
                .document(newEvent.getClientId())
                .collection("Client Appointments")
                .document(eventAddActivity.appointmentID)
                .delete();
    }

    public static DocumentReference getData(String IDclient){
        DocumentReference docRef = database.collection("Appointments").document(IDclient);
        return docRef;
    }
}
