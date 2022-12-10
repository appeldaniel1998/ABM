package com.example.abm.Appointments.AppointmentCalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.abm.BaseActivity;
import com.example.abm.R;

import java.time.LocalTime;
import java.util.Locale;

public class EventEditActivity extends BaseActivity {
    ////Activity_appointments_calender_event_edit
    //Add new event and Time picker functions

    private EditText eventNameET;//event Name Edit Text
    private TextView eventDateTV, eventTimeTV; //2 text views
    private LocalTime time;
    Button timeButton;//time picker
    int hour, minute;//time picker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calender_event_edit);
        super.initMenuSideBar();

        initWidgets();//find all views by their id
        time=LocalTime.now();//display current time before change it due to time picker
        eventDateTV.setText("Date: "+ CalendarUtils.formatteDate(CalendarUtils.selectedDate));//defined the date to be the date that the user selected
        eventTimeTV.setText("Time: "+ CalendarUtils.formatteTime(time));//defined the time to be the time that the user selected
        timeButton=findViewById(R.id.timeButton);


    }

    private void initWidgets()
    {//init widget function- finds all of those views by their id
        eventNameET=findViewById(R.id.eventNameET);
        eventDateTV=findViewById(R.id.eventDateTV);
        eventTimeTV=findViewById(R.id.eventTimeTV);

    }

    public void saveNewEventAction(View view)
    {//save the event the user created
        String eventName=eventNameET.getText().toString();//get the name of the event
        Event newEvent=new Event (eventName, Event.localDateToInt(CalendarUtils.selectedDate), Event.localTimeToInt(time));//create new event
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
}