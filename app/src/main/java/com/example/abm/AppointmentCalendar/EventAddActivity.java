package com.example.abm.AppointmentCalendar;

import static com.example.abm.AppointmentCalendar.EventDatabaseUtils.databaseAppointmentsRetrieval;
import static com.example.abm.AppointmentCalendar.EventDatabaseUtils.docRefFunc;
import static com.example.abm.AppointmentCalendar.EventDatabaseUtils.getData;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.abm.BaseActivity;
import com.example.abm.R;
import com.example.abm.Utils.DatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
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

    private static TextView appointmentType;//event Name Edit Text
    //    private TextView appointmentType;//event Name Edit Text
    private static TextView ClientName;//Client Name Edit Text
    private TextView eventDateTV, eventTimeTV; //2 text views
    private LocalTime time;
    int hour, minute;//time picker
    private ProgressDialog progressDialog;

    static ArrayList<String> appointmentTypes;//drop down list of types
    static ArrayList<String> clientNames;//drop down list of clients names

    AutoCompleteTextView appointmentTypeAutoCompleteTxt;//drop down list of Clients
    ArrayAdapter<String> adapterItems;//drop down list of types

//    FirebaseFirestore database;
//    FirebaseAuth auth;
    private Button Savebutton;
    String appointmentID;

    private TextInputLayout clientsWrapper;
    private TextInputLayout appointmentTypeWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //to create the drop down list we used the link:
        //https://www.youtube.com/watch?v=EBhmRaa8nhE
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calender_event_add);
        super.initMenuSideBar();

        //database = super.getCurrDatabase();
        ProgressDialog progressDialogAppointmentTypes = ProgressDialog.show(this, "Add Appointment", "Loading, please wait....", true);
        ProgressDialog progressDialogClientNames = ProgressDialog.show(this, "Add Appointment", "Loading, please wait....", true);
//        appointmentTypes = CalendarDatabaseUtils.getAppointmentTypesFromDB(database, progressDialogAppointmentTypes); // get appointment types from database to present in the dropdown list
//        clientNames = CalendarDatabaseUtils.getClientNamesFromDB(progressDialogClientNames); // get client names to present in the dropdown list
//
//        ProgressDialog progressDialog = ProgressDialog.show(this, "Add Appointment", "Loading, please wait....", true);
//        //auth = super.getCurrFirebaseAuth();
//        database.collection("Clients").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
//            Client currUser = documentSnapshot.toObject(Client.class);
//            if (!currUser.getManager()) { // user is a client
//                findViewById(R.id.clientsWrapper).setVisibility(View.GONE);
//                findViewById(R.id.clientname).setVisibility(View.GONE);
//            }
//            progressDialog.dismiss();
//        });
        EventDatabaseUtils.getAppType(this,progressDialogAppointmentTypes,progressDialogClientNames);
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

        appointmentTypeAutoCompleteTxt.setOnItemClickListener((parent, view, position, id) -> {
            //String item = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), "Client selected! ", Toast.LENGTH_SHORT).show();//"Item: "+item,Toast.LENGTH_SHORT).show()
        });

        Button deleteButton = findViewById(R.id.deleteButton);
        this.appointmentID = getIntent().getStringExtra("appointmentID");
        if (!appointmentID.equals("-1")) {
            deleteButton.setOnClickListener(v -> {
                Event eventNew = Event.getEvent(appointmentID);
                String idClientOfAppointment = eventNew.getClientId();
                //DocumentReference docRef = database.collection("Appointments").document(idClientOfAppointment);
                DocumentReference docRef =docRefFunc(idClientOfAppointment);
                docRef.collection("Client Appointments").document(appointmentID).delete();
                startActivity(new Intent(EventAddActivity.this, WeekViewActivity.class));//back to week view display
                finish();//close the activity
            });
            initValuesOfLayout(Event.getEvent(appointmentID));
        } else {
            deleteButton.setVisibility(View.GONE);
        }


    }

    void findView()
    {
        findViewById(R.id.clientsWrapper).setVisibility(View.GONE);
        findViewById(R.id.clientname).setVisibility(View.GONE);
    }
    private void initValuesOfLayout(Event eventNew) {
        clientsWrapper = findViewById(R.id.clientsWrapper);
        appointmentTypeWrapper = findViewById(R.id.appointmentTypeWrapper);

        //function display the data of event I would like to edit ,after clicking on the event
        String fullName = eventNew.getClientName();//get the client name
        ClientName.setText(fullName);//set text of client name
        eventDateTV.setText(DatePicker.intToString(eventNew.getDate()));//set text of event date
        eventTimeTV.setText(eventNew.getFormattedStartTime());//set text of event start time
        appointmentType.setText(eventNew.getAppointmentType());//set text of appointment type
        //doneEditingButton.setText("Done");

//        clientsWrapper.getEditText().setText(fullName);
//        appointmentTypeWrapper.getEditText().setText(eventNew.getAppointmentType());

        AutoCompleteTextView autoCompleteTextViewClients = findViewById(R.id.auto_complete_txt_client);
        ArrayAdapter<String> adapterItemsClients = new ArrayAdapter<>(this, R.layout.list_item, clientNames);
        autoCompleteTextViewClients.setAdapter(adapterItemsClients);

        AutoCompleteTextView autoCompleteTextViewAppTypes = findViewById(R.id.auto_complete_txt);
        ArrayAdapter<String> adapterItemsAppTypes = new ArrayAdapter<>(this, R.layout.list_item, appointmentTypes);
        autoCompleteTextViewAppTypes.setAdapter(adapterItemsAppTypes);


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

    //save the event the user created
    //public void saveNewEventAction(View view)
        public void saveNewEventAction(View view)
    {
            boolean isManager= EventDatabaseUtils.saveData();
            //Converting fields to text
            String eventName = appointmentType.getText().toString();//get the name of the event
            String cliName;
            String IDclient;
            if (isManager) { //if user is manager
                cliName = ClientName.getText().toString();//get the client name
                IDclient = WeekViewActivity.getKey(cliName);
            } else { //if user is client
                cliName = clientNames.get(0);
                IDclient = getCurrFirebaseAuth().getUid();
            }

            String uuid;
            Event newEvent;
            if (this.appointmentID.equals("-1")) { // adding new event
                uuid = UUID.randomUUID().toString().replace("-", "");//Create a random UID for the new event
                newEvent = new Event(uuid, eventName, cliName, Event.localDateToInt(CalendarUtils.selectedDate), Event.timeStringToInt(eventTimeTV.getText().toString()), IDclient);//create new event
                Event.eventsList.add(newEvent);//add event to the list of events in this day-For displaying
            } else { //editing existing event
                uuid = this.appointmentID;
                newEvent = Event.getEvent(EventAddActivity.this.appointmentID);
                databaseAppointmentsRetrieval(newEvent,this);
//                database.collection("Appointments")
//                        .document(newEvent.getClientId())
//                        .collection("Client Appointments")
//                        .document(EventAddActivity.this.appointmentID)
//                        .delete();

                newEvent.setEvent(uuid, eventName, cliName, Event.localDateToInt(CalendarUtils.selectedDate), Event.timeStringToInt(eventTimeTV.getText().toString()), IDclient);
            }

            //DocumentReference docRef = database.collection("Appointments").document(IDclient);

            DocumentReference docRef=getData(IDclient);
            Map<String, Object> newEventMap = new HashMap<>();
            newEventMap.put("appointmentId", newEvent.getAppointmentId());
            newEventMap.put("appointmentType", newEvent.getAppointmentType());
            newEventMap.put("clientId", newEvent.getClientId());
            newEventMap.put("clientName", newEvent.getClientName());
            newEventMap.put("date", newEvent.getDate());
            newEventMap.put("startTime", newEvent.getStartTime());
            docRef.collection("Client Appointments").document(uuid).set(newEventMap).addOnSuccessListener(unused -> {
                Map<String, Object> data_temp = new HashMap<>();
                data_temp.put("1", 1);
                docRef.set(data_temp, SetOptions.merge());
            }); //adding event data_temp to database
            EventAddActivity.this.startActivity(new Intent(EventAddActivity.this, WeekViewActivity.class));//back to week view display
            finish();//close the activity
        }
    }
//    public static void convertingFielsToText(boolean isManager)
//    {
////        String eventName = appointmentType.getText().toString();//get the name of the event
//        String cliName;
//        String IDclient;
//        if (isManager) { //if user is manager
//            cliName = ClientName.getText().toString();//get the client name
//            IDclient = WeekViewActivity.getKey(cliName);
//        } else { //if user is client
//            cliName = clientNames.get(0);
//            IDclient = getCurrFirebaseAuth().getUid();
//        }
//    }
