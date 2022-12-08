package com.example.abm.Appointments.AppointmentCalender;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abm.R;

import java.time.LocalTime;

public class EventEditActivity extends AppCompatActivity {
    ////Activity_appointments_calender_event_edit

    private EditText eventNameET;//event Name Edit Text
    private TextView eventDateTV, eventTimeTV; //2 text views
    private LocalTime time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calender_event_edit);
        initWidgets();
        time=LocalTime.now();/////need to change!
        eventDateTV.setText("Date: "+CalenderUtils.formatteDate(CalenderUtils.selectedDate));//defined the date to be the date that the user selected
        eventTimeTV.setText("Time: "+CalenderUtils.formatteTime(time));//defined the time to be the time that the user selected


    }

    private void initWidgets()
    {//init widget function- finds all of those views by their id
        eventNameET=findViewById(R.id.eventNameET);
        eventDateTV=findViewById(R.id.eventDateTV);
        eventTimeTV=findViewById(R.id.eventTimeTV);

    }

    public void saveEventAction(View view)
    {
        String eventName=eventNameET.getText().toString();
        Event newEvent=new Event (eventName, CalenderUtils.selectedDate, time);
        Event.eventsList.add(newEvent);
        finish();//close the activity
    }
}