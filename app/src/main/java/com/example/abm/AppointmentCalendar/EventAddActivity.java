package com.example.abm.AppointmentCalendar;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.abm.BaseActivity;
import com.example.abm.Clients.Client;
import com.example.abm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class EventAddActivity extends BaseActivity {
    ////Activity_appointments_calender_event_add
    //Add new event , Time picker functions, and drop down lists

    private TextView appointmentType;//event Name Edit Text
//    private TextView appointmentType;//event Name Edit Text
    private TextView ClientName;//Client Name Edit Text
    private TextView eventDateTV, eventTimeTV; //2 text views
    private LocalTime time;
    Button timeButton, saveButton, deleteButton;//time picker
    int hour, minute;//time picker
    private ProgressDialog progressDialog;

    ArrayList<String> appointmentTypes;//drop down list of types
    ArrayList<String> clientNames;//drop down list of clients names

    AutoCompleteTextView appointmentTypeAutoCompleteTxt;//drop down list of Clients
    ArrayAdapter<String> adapterItems;//drop down list of types

    FirebaseFirestore database;
    FirebaseAuth auth;
    private Button Savebutton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //to create the drop down list we used the link:
        //https://www.youtube.com/watch?v=EBhmRaa8nhE
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calender_event_add);
        super.initMenuSideBar();

        database = super.getCurrDatabase();
        ProgressDialog progressDialogAppointmentTypes = ProgressDialog.show(this, "Add Appointment", "Loading, please wait....", true);
        ProgressDialog progressDialogClientNames = ProgressDialog.show(this, "Add Appointment", "Loading, please wait....", true);
        appointmentTypes = CalendarDatabaseUtils.getAppointmentTypesFromDB(database, progressDialogAppointmentTypes); // get appointment types from database to present in the dropdown list
        clientNames = CalendarDatabaseUtils.getClientNamesFromDB(progressDialogClientNames); // get client names to present in the dropdown list

        ProgressDialog progressDialog = ProgressDialog.show(this, "Add Appointment", "Loading, please wait....", true);
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
        //make it availble to click on each item in events list and edit or delete it
        //java
        //https://github.com/easy-tuto/MyListViewDemo/blob/master/app/src/main/java/com/example/mylistviewdemo/MainActivity.java
        //xml
        //https://github.com/easy-tuto/MyListViewDemo/blob/master/app/src/main/res/layout/activity_main.xml

//        setContentView(R.layout.activity_appointments_calender_week_view);
//        ListView lv = findViewById(R.id.eventListView);
//        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,Event.eventsList);
//        lv.setAdapter(arrayAdapter);

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

    public void saveNewEventAction(View view) {//save the event the user created
        //Converting fields to text
        String eventName = appointmentType.getText().toString();//get the name of the event
        String cliName = ClientName.getText().toString();//get the client name
        String IDclient=WeekViewActivity.getkey(cliName);
//        //timeButton = findViewById(R.id.timeButton);
        final String uuid = UUID.randomUUID().toString().replace("-", "");//Create a random UID for the new event
        //final String uuid=
        Event newEvent = new Event(uuid, eventName, cliName, Event.localDateToInt(CalendarUtils.selectedDate), Event.timeStringToInt(eventTimeTV.getText().toString()),IDclient);//create new event
        Event.eventsList.add(newEvent);//add event to the list of events in this day-For displaying
        DocumentReference docRef=database.collection("Appointments").document(IDclient);

        Map<String, Object> newEventMap = new HashMap<>();
        newEventMap.put("appointmentId", newEvent.getAppointmentId());
        newEventMap.put("appointmentType", newEvent.getAppointmentType());
        newEventMap.put("clientId", newEvent.getClientId());
        newEventMap.put("clientName", newEvent.getClientName());
        newEventMap.put("date", newEvent.getDate());
        newEventMap.put("startTime", newEvent.getStartTime());
        docRef.collection("Client Appointments").document(uuid).set(newEventMap).addOnSuccessListener(unused -> {
            Map<String, Object> data = new HashMap<>();
            data.put("1", 1);
            docRef.set(data, SetOptions.merge());
//            Toast.makeText(EventAddActivity.this, "Save was clicked!", Toast.LENGTH_SHORT).show();
        }); //adding event data to database
//        Toast.makeText(this, "Save was clicked!", Toast.LENGTH_SHORT).show();
        EventAddActivity.this.startActivity(new Intent(EventAddActivity.this, WeekViewActivity.class));//back to week view display
        finish();//close the activity

    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                //timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));//display the chosen time on button, no need
                //defined the time to be the time that the user selected next to "Time:"
                eventTimeTV.setText("Time: " + String.format(Locale.getDefault(), "%02d:%02d", hour, minute));

            }
        };


        // int style = AlertDialog.THEME_HOLO_DARK; //different style of picker uf we want to change

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void deleteEventAction(View view)
//    public void deleteEventAction(Event event)
    //by pressing delete button this will delete th event from DB and from the weekly display list of event.
    {
        String eventName = appointmentType.getText().toString();//get the name of the event
        String cliName = ClientName.getText().toString();//get the client name
        String IDclient=WeekViewActivity.getkey(cliName);//(cliName);
//        //timeButton = findViewById(R.id.timeButton);
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        Event event = new Event(uuid, eventName, cliName, Event.localDateToInt(CalendarUtils.selectedDate), Event.timeStringToInt(eventTimeTV.getText().toString()),IDclient);//create new event

        ////////////////////////////////////////////////////////
        Event.eventsList.remove(event);//remove the event from eventsList
        Toast.makeText(this, "Event was deleted!", Toast.LENGTH_SHORT).show();
        EventAddActivity.this.startActivity(new Intent(EventAddActivity.this, WeekViewActivity.class));//back to the week view display
        finish();//close the activity
    }


//     Log.i("testTag","before start adapter");
//    StringArrayAdapter ad = new StringArrayAdapter (members,this);
//        Log.i("testTag","after start adapter");
//        Log.i("testTag","set adapter");
//        lv.setAdapter(ad);
//   lv.setOnItemClickListener(new OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position,
//        long id) {
//            ListEntry entry= (ListEntry) parent.getAdapter().getItem(position);
//            Intent intent = new Intent(MainActivity.this, SendMessage.class);
//            String message = entry.getMessage();
//            intent.putExtra(EXTRA_MESSAGE, message);
//            startActivity(intent);
//        }
//    });


}