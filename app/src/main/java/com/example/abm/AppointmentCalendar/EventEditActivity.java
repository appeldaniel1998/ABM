package com.example.abm.AppointmentCalendar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.abm.BaseActivity;
import com.example.abm.Clients.Client;
import com.example.abm.Clients.EditClientActivity;
import com.example.abm.Clients.SingleClientViewActivity;
import com.example.abm.R;
import com.example.abm.Utils.DatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class EventEditActivity extends BaseActivity{//} implements AdapterView.OnItemClickListener {
    FirebaseFirestore database;
    FirebaseAuth auth;
    private StorageReference storageReference;
    Event event;
    private TextView eventDateTV;
    private TextView eventTimeTV;
    private TextView appointmentType;
    private TextView ClientName;
    private Button doneEditingButton;
    private DatePickerDialog datePickerDialog;
    String appointmentID="";
    ArrayList<String> appointmentTypes;//drop down list of types
    ArrayList<String> clientNames;//drop down list of clients names
    AutoCompleteTextView appointmentTypeAutoCompleteTxt;//drop down list of Clients
    ArrayAdapter<String> adapterItems;//drop down list of types
    private LocalTime time;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_appointments_calender_event_edit);
        super.initMenuSideBar();


        database = super.getCurrDatabase();
        storageReference = super.getStorageReference();
        ProgressDialog progressDialogAppointmentTypes = ProgressDialog.show(this, "Edit Appointment", "Loading, please wait....", true);
        ProgressDialog progressDialogClientNames = ProgressDialog.show(this, "Edit Appointment", "Loading, please wait....", true);
        //display information about the event
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
        appointmentType = findViewById(R.id.auto_complete_txt);
        ClientName = findViewById(R.id.auto_complete_txt_client);


        appointmentTypes = CalendarDatabaseUtils.getAppointmentTypesFromDB(database, progressDialogAppointmentTypes); // get appointment types from database to present in the dropdown list
        clientNames = CalendarDatabaseUtils.getClientNamesFromDB(progressDialogClientNames); // get client names to present in the dropdown list

//        Intent intent = getIntent();
//        appointmentID = intent.getStringExtra("appointmentID");
        String appointmentID = getIntent().getStringExtra("appointmentID");


//////////////////////////////////////////////
        database.collection("Appointments").document(appointmentID)
                .get()
                .addOnSuccessListener(documentSnapshot -> { // If client info retrieved successfully from the DB
                    event = documentSnapshot.toObject(Event.class);
                    initValuesOfLayout();
                    progressDialogAppointmentTypes.dismiss();
                    progressDialogClientNames.dismiss();
//                    doneEditingButton.setOnClickListener(v -> {
//                        Client userToAdd = new Client(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(),
//                                phoneNumber.getText().toString(), address.getText().toString(),
//                                DatePicker.stringToInt(birthday.getText().toString()), clientUID); //creating a new user
//                        database.collection("Clients").document(clientUID).set(userToAdd); //adding user data to database
//
//                        Intent myIntent = new Intent(EditClientActivity.this, SingleClientViewActivity.class);
//                        myIntent.putExtra("clientUID", clientUID); //Optional parameters
//                        EditClientActivity.this.startActivity(myIntent);
//                        finish();
//                    });
                });



        //////////////////////////////////////////////




        ProgressDialog progressDialog = ProgressDialog.show(this, "Edit Appointment", "Loading, please wait....", true);
        auth = super.getCurrFirebaseAuth();
        database.collection("Clients").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            Client currUser = documentSnapshot.toObject(Client.class);
            if (!currUser.getManager()) { // user is a client
                findViewById(R.id.clientsInputField).setVisibility(View.GONE);
                findViewById(R.id.clientname).setVisibility(View.GONE);
            }
            progressDialog.dismiss();
        });

        initWidgets();//find all views by their id
        time = LocalTime.now();//display current time before change it due to time picker

        eventDateTV.setText("Date: " + CalendarUtils.formatteDate(CalendarUtils.selectedDate));//defined the date to be the date that the user selected
        eventTimeTV.setText("Time: " + CalendarUtils.formatteTime(time));//defined the time to be the time that the user selected


        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, appointmentTypes);
        appointmentTypeAutoCompleteTxt.setAdapter(adapterItems);

        appointmentTypeAutoCompleteTxt.setOnItemClickListener((parent, view, position, id) -> {
            //String client_name = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), "Type selected! ", Toast.LENGTH_SHORT).show();//"Item: "+item,Toast.LENGTH_SHORT).show()
        });
        eventTimeTV.setText("Time: " + CalendarUtils.formatteTime(time));//defined the time to be the time that the user selected

        //activate the drop down list for Clients names
        appointmentTypeAutoCompleteTxt = findViewById(R.id.auto_complete_txt_client);

        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, clientNames);
        appointmentTypeAutoCompleteTxt.setAdapter(adapterItems);

        appointmentTypeAutoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Client selected! ", Toast.LENGTH_SHORT).show();//"Item: "+item,Toast.LENGTH_SHORT).show()
            }
        });

//
//        database = super.getCurrDatabase();
//        ProgressDialog progressDialogAppointmentTypes = ProgressDialog.show(this, "Add Appointment", "Loading, please wait....", true);
//        ProgressDialog progressDialogClientNames = ProgressDialog.show(this, "Add Appointment", "Loading, please wait....", true);
//        appointmentTypes = CalendarDatabaseUtils.getAppointmentTypesFromDB(database, progressDialogAppointmentTypes); // get appointment types from database to present in the dropdown list
//        clientNames = CalendarDatabaseUtils.getClientNamesFromDB(progressDialogClientNames); // get client names to present in the dropdown list
//
//        //Import access to DB
//        database = super.getCurrDatabase();
//        storageReference = super.getStorageReference();
//
//        //Loading symbole
//        ProgressDialog progressDialog;
//        progressDialog = ProgressDialog.show(this, "Edit Event", "Loading, please wait....", true);
//
//        //display information about the event
//        eventDateTV = findViewById(R.id.eventDateTV);
//        eventTimeTV = findViewById(R.id.eventTimeTV);
//        appointmentType = findViewById(R.id.auto_complete_txt);
//        ClientName = findViewById(R.id.auto_complete_txt_client);
//
//        doneEditingButton = findViewById(R.id.Savebutton);
//
//        // Initiating date picks handling
//        datePickerDialog = DatePicker.initDatePicker(eventDateTV, this);
//
//        Intent intent = getIntent();
//        clientUID = intent.getStringExtra("clientUID");
//
//        //ProgressDialog imageProgressDialog = ProgressDialog.show(this, "Edit Event", "Loading, please wait....", true);
//        //StorageReference profilePicReference = storageReference.child("Clients").child(this.clientUID).child("profile.jpg");
//        progressDialog.dismiss();
//        //Istopped_here=
//        //* *EDIT* *



        //ListView listview = findViewById(R.id.eventListView);
        //listview.setOnItemClickListener(this);


    }

    private void initValuesOfLayout()
    {
        //function display the data of event I would like to edit ,after clicking on the event
        String fullName = event.getClientName();//get the client name
        ClientName.setText(fullName);//set text of client name
        eventDateTV.setText(event.getDate());//set text of event date
        eventTimeTV.setText(event.getStartTime());//set text of event start time
        appointmentType.setText(event.getAppointmentType());//set text of appointment type
        doneEditingButton.setText("Done");


    }

    private void initWidgets() {//init widget function- finds all of those views by their id
        //eventNameET=findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
        appointmentType = findViewById(R.id.auto_complete_txt);
        ClientName = findViewById(R.id.auto_complete_txt_client);

        //activate the drop down list for appointment type
        appointmentTypeAutoCompleteTxt = findViewById(R.id.auto_complete_txt);
    }
//    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
//        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
//        // Then you start a new Activity via Intent
//        Intent intent = new Intent();
//        intent.setClass(this, Event.class);
//        intent.putExtra("position", position);
//        // Or / And
//        intent.putExtra("id", id);
//        startActivity(intent);
//    }
}

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        //to create the drop down list we used the link:
//        //https://www.youtube.com/watch?v=EBhmRaa8nhE
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_appointments_calender_event_edit);//display the activity
//        super.initMenuSideBar();//display menu bar
//
//
//
//
//        database = super.getCurrDatabase();
//        ProgressDialog progressDialogAppointmentTypes = ProgressDialog.show(this, "Edit Appointment", "Loading, please wait....", true);
//        ProgressDialog progressDialogClientNames = ProgressDialog.show(this, "Edit Appointment", "Loading, please wait....", true);
//
//        ProgressDialog progressDialog = ProgressDialog.show(this, "Edit Appointment", "Loading, please wait....", true);
//        auth = super.getCurrFirebaseAuth();
//        database.collection("Clients").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
//            Client currUser = documentSnapshot.toObject(Client.class);
//            if (!currUser.getManager()) { // user is a client
//                findViewById(R.id.clientsInputField).setVisibility(View.GONE);
//                findViewById(R.id.clientname).setVisibility(View.GONE);
//            }
//            progressDialog.dismiss();
//        });
//
////        initWidgets();//find all views by their id
////        time = LocalTime.now();//display current time before change it due to time picker
////
////        eventDateTV.setText("Date: " + CalendarUtils.formatteDate(CalendarUtils.selectedDate));//defined the date to be the date that the user selected
////        eventTimeTV.setText("Time: " + CalendarUtils.formatteTime(time));//defined the time to be the time that the user selected
////
////
////        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, appointmentTypes);
////        appointmentTypeAutoCompleteTxt.setAdapter(adapterItems);
////
////        appointmentTypeAutoCompleteTxt.setOnItemClickListener((parent, view, position, id) -> {
//            //String client_name = parent.getItemAtPosition(position).toString();
////            Toast.makeText(getApplicationContext(), "Type selected! ", Toast.LENGTH_SHORT).show();//"Item: "+item,Toast.LENGTH_SHORT).show()
////        });
////        eventTimeTV.setText("Time: " + CalendarUtils.formatteTime(time));//defined the time to be the time that the user selected
////
////        //activate the drop down list for Clients names
////        appointmentTypeAutoCompleteTxt = findViewById(R.id.auto_complete_txt_client);
////
////        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, clientNames);
////        appointmentTypeAutoCompleteTxt.setAdapter(adapterItems);
////
////        appointmentTypeAutoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                //String item = parent.getItemAtPosition(position).toString();
////                Toast.makeText(getApplicationContext(), "Client selected! ", Toast.LENGTH_SHORT).show();//"Item: "+item,Toast.LENGTH_SHORT).show()
////            }
////        });
//        //make it availble to click on each item in events list and edit or delete it
//        //java
//        //https://github.com/easy-tuto/MyListViewDemo/blob/master/app/src/main/java/com/example/mylistviewdemo/MainActivity.java
//        //xml
//        //https://github.com/easy-tuto/MyListViewDemo/blob/master/app/src/main/res/layout/activity_main.xml
//
////        setContentView(R.layout.activity_appointments_calender_week_view);
////        ListView lv = findViewById(R.id.eventListView);
////        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,Event.eventsList);
////        lv.setAdapter(arrayAdapter);
//    }
//}