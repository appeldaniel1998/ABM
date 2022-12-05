package com.example.abm.Appointments.AppointmentCalender;

import static com.example.abm.Appointments.AppointmentCalender.CalenderUtils.daysInMonthArray;
import static com.example.abm.Appointments.AppointmentCalender.CalenderUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abm.Appointments.AppointmentCalender.CalendarAdapter;
import com.example.abm.R;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivityTry extends AppCompatActivity implements CalendarAdapter.OnItemListener
{//Activity_appointments_calender_main

    //To build calender I used:
    // https://www.youtube.com/watch?v=Ba0Q-cK1fJo
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calender_main);//5.55 in the video: https://www.youtube.com/watch?v=Ba0Q-cK1fJo
        initWidgets();
        CalenderUtils.selectedDate=LocalDate.now();
        setMonthView();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(CalenderUtils.selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(CalenderUtils.selectedDate);

//        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);// we're going to have 7 colunms in our recycle view
        calendarRecyclerView.setLayoutManager(layoutManager);
//        calendarRecyclerView.setAdapter(calendarAdapter);
    }


    public void previousMonthAction(View view)
    {
        CalenderUtils.selectedDate = CalenderUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        CalenderUtils.selectedDate = CalenderUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(CalenderUtils.selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    public void weeklyAction(View view)
    {
        startActivity(new Intent(this,WeekViewActivity.class));
    }
}