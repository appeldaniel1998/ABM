package com.example.abm.AppointmentType;

import android.app.ProgressDialog;
import android.widget.TextView;

import com.example.abm.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class AppointmentTypeDatabaseUtils {
    private static final FirebaseFirestore database = FirebaseFirestore.getInstance();

    public static void getAppointmentTypes(AppointmentTypesMainActivity appointmentTypesMainActivity) {
        database.collection("Appointment Types").orderBy("typeName").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                setArrayListFromDb(task);
                AppointmentTypesMainActivity.onSuccessGetFromDB(appointmentTypesMainActivity);
            }
        });
    }

    private static void setArrayListFromDb(Task<QuerySnapshot> task) {
        for (QueryDocumentSnapshot document : task.getResult()) {
            Map<String, Object> data = document.getData();
            String name = (String) data.get("typeName");
            String price = (String) data.get("price");
            String duration = (String) data.get("duration");
            AppointmentTypesMainActivity.appointmentTypes.add(new AppointmentType(name, price, duration));
        }
    }

    public static void getSingleAppointmentType(String currAppointmentTypeName, AddEditAppointmentTypeActivity addEditAppointmentTypeActivity, ProgressDialog progressDialog) {
        database.collection("Appointment Types").document(currAppointmentTypeName)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    AppointmentType appointmentType = documentSnapshot.toObject(AppointmentType.class);
                    addEditAppointmentTypeActivity.onSuccessGetSingleAppointmentTypeFromDB(appointmentType, progressDialog);
                });
    }

    public static void deleteAppointmentType(String currAppointmentTypeName, AddEditAppointmentTypeActivity addEditAppointmentTypeActivity) {
        database.collection("Appointment Types").document(currAppointmentTypeName).delete().addOnSuccessListener(unused -> {
            addEditAppointmentTypeActivity.addNewItem();
        });
    }

    public static void addAppointmentType(String appointmentTypeName, AppointmentType appointmentType) {
        database.collection("Appointment Types").document(appointmentTypeName).set(appointmentType); //adding appointment type data to database
    }
}
