package com.example.abm.Appointments;

import android.os.Bundle;

import com.example.abm.BaseActivity;
import com.example.abm.Clients.Client;
import com.example.abm.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AppointmentsMainActivity extends BaseActivity {

    private ArrayList<Appointment> appointments;

    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_main);
        super.initMenuSideBar();

        database = super.getCurrDatabase();
        appointments = new ArrayList<>();

//        getDataFromDB();

    }

    private void getDataFromDB() {
        FirebaseUser user = super.getCurrFirebaseAuth().getCurrentUser();
        if (user != null) {
            String UserUid = user.getUid();
            database.collection("Clients").document(UserUid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                Client currUser = documentSnapshot.toObject(Client.class);
                if (currUser != null) {
                    if (currUser.getManager()) { // user is a manager
                        ArrayList<String> clientsWithAppointments = new ArrayList<>();
                        CollectionReference collectionRef = database.collection("Appointments");
                        collectionRef.get() //get all appointments (with documents labeled with client ID
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            clientsWithAppointments.add((String) document.getData().get("clientId"));
                                        }

                                        for (int i = 0; i < clientsWithAppointments.size(); i++) {
                                            collectionRef.document(clientsWithAppointments.get(i)).collection("Appointments")
                                                    .get()
                                                    .addOnCompleteListener(task1 -> {
                                                        if (task1.isSuccessful()) {
                                                            addToAppointmentList(task1);
                                                        }
                                                    });
                                        }
                                    }
                                });
                    } else { // user is a client
                        database.collection("Appointments").document(currUser.getUid()).collection("Appointments").get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                addToAppointmentList(task);
                            }
                        });
                    }
                }
            });
        }
    }

    private void addToAppointmentList(Task<com.google.firebase.firestore.QuerySnapshot> task) {
        for (QueryDocumentSnapshot document : task.getResult()) {
            Map<String, Object> data = document.getData();
            String appointmentId = (String) data.get("appointmentId");
            String appointmentType = (String) data.get("appointmentType");
            String clientId = (String) data.get("clientId");
            String date = (String) data.get("date");
            String startTime = (String) data.get("startTime");
            appointments.add(new Appointment(appointmentId, date, startTime, appointmentType, clientId));
        }
    }
}