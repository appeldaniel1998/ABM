package com.example.abm.Appointments.AppointmentCalender;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.abm.R;
import android.app.TimePickerDialog.OnTimeSetListener;

import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Locale;

public class TimePickerMain extends AppCompatActivity {
////Activity_appointments_calender_time_picker

    // The links I used:
    //https://www.youtube.com/watch?v=c6c1giRekB4&t=0s
    //https://github.com/codeWithCal/TimePickerAndroidStudio/blob/master/app/src/main/res/layout/activity_main.xml
    //
    Button timeButton;
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calender_time_picker);
        timeButton = findViewById(R.id.timeButton);
    }

    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK; //different style of picker uf we want to change

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}