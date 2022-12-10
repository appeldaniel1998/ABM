package com.example.abm.Appointments.AppointmentCalendar;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import com.example.abm.BaseActivity;
import com.example.abm.R;

import java.time.LocalTime;
import java.util.Locale;
import java.util.UUID;

public class EventEditActivity extends BaseActivity {
    ////Activity_appointments_calender_event_edit
    //Add new event , Time picker functions, and drop down lists

    //private EditText eventNameET;//event Name Edit Text
    private TextView appType;//event Name Edit Text
    private TextView ClientName;//event Name Edit Text
    private TextView eventDateTV, eventTimeTV; //2 text views
    private LocalTime time;
    Button timeButton;//time picker
    int hour, minute;//time picker
    String[] appTypes =  {"Manicure","Massage","Pedicure"};//drop down list of types
    String[] clientsNames =  {"change","change","change"};//drop down list of clients names

    AutoCompleteTextView autoCompleteTxt;//drop down list of types
    ArrayAdapter<String> adapterItems;//drop down list of types

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //to create the drop down list we used the link:
        //https://www.youtube.com/watch?v=EBhmRaa8nhE
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calender_event_edit);
        super.initMenuSideBar();

        initWidgets();//find all views by their id
        time=LocalTime.now();//display current time before change it due to time picker
        eventDateTV.setText("Date: "+ CalendarUtils.formatteDate(CalendarUtils.selectedDate));//defined the date to be the date that the user selected
        eventTimeTV.setText("Time: "+ CalendarUtils.formatteTime(time));//defined the time to be the time that the user selected

        //activate the drop down list for appointment type
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, appTypes);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String client_name = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Type selected! ",Toast.LENGTH_SHORT).show();//"Item: "+item,Toast.LENGTH_SHORT).show()
            }
        });
        eventTimeTV.setText("Time: "+ CalendarUtils.formatteTime(time));//defined the time to be the time that the user selected

        //activate the drop down list for Clients names
        autoCompleteTxt = findViewById(R.id.auto_complete_txt_client);

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, clientsNames);
        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Client selected! ",Toast.LENGTH_SHORT).show();//"Item: "+item,Toast.LENGTH_SHORT).show()
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

    private void initWidgets()
    {//init widget function- finds all of those views by their id
        //eventNameET=findViewById(R.id.eventNameET);
        eventDateTV=findViewById(R.id.eventDateTV);
        eventTimeTV=findViewById(R.id.eventTimeTV);
        appType=findViewById(R.id.auto_complete_txt);
        ClientName=findViewById(R.id.auto_complete_txt_client);
    }

    public void saveNewEventAction(View view)
    {//save the event the user created
        String eventName=appType.getText().toString();//get the name of the event
        String cliName=ClientName.getText().toString();//get the client name
        timeButton=findViewById(R.id.timeButton);
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        Event newEvent=new Event (uuid,eventName,cliName, Event.localDateToInt(CalendarUtils.selectedDate), Event.timeStringToInt(eventTimeTV.getText().toString()));//create new event
        Event.eventsList.add(newEvent);//add event to the list of events in this day
        finish();//close the activity
    }

    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                //timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));//display the chosen time on button, no need
                //defined the time to be the time that the user selected next to "Time:"
                eventTimeTV.setText("Time: "+String.format(Locale.getDefault(), "%02d:%02d",hour, minute));

            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK; //different style of picker uf we want to change

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
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