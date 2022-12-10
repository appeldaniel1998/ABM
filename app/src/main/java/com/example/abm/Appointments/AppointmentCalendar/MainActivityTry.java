package com.example.abm.Appointments.AppointmentCalendar;

import static com.example.abm.Appointments.AppointmentCalendar.CalendarUtils.daysInMonthArray;
import static com.example.abm.Appointments.AppointmentCalendar.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.abm.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class MainActivityTry extends AppCompatActivity implements CalendarAdapter.OnItemListener
{//Activity_appointments_calender_main

    //To build monthly calender I used:
    // https://www.youtube.com/watch?v=Ba0Q-cK1fJo
    //https://github.com/codeWithCal/CalendarTutorialAndroidStudio/tree/WeeklyCalendar/app/src/main/java/codewithcal/au/calendarappexample
    //To build weekly calender I used:
    //https://www.youtube.com/watch?v=knpSbtbPz3o
    //To build daily calender I used:
    //https://www.youtube.com/watch?v=Aig99t-gNqM&t=0s

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calender_main);//5.55 in the video: https://www.youtube.com/watch?v=Ba0Q-cK1fJo
        initWidgets();//define recycleviews
        //start to show the monthly calendar from current date
        CalendarUtils.selectedDate=LocalDate.now();//current date
        setMonthView();
    }
    //define the calendar recycle view and the title (between 2 arrows)
    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);//id from activity_appointments_calender_main.xml
        monthYearText = findViewById(R.id.monthYearTV);//text between 2 arrows
    }
//define monthly viewing
    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);// we're going to have 7 colunms in our recycle view
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

//by clicking on the 'back arrow' in month view this function will show us prev month
    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);//prev month
        setMonthView();
    }
    //by clicking on the 'back arrow' in month view this function will show us prev month
    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);//next month
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)//7:39 : https://www.youtube.com/watch?v=knpSbtbPz3o
    {//
        if (date!=null)//we will have the days before and after the month
        {
            CalendarUtils.selectedDate=date;
            setMonthView();
        }

    }
//when clicking on 'WEEKLY' button
    public void weeklyAction(View view)
    {
        startActivity(new Intent(this,WeekViewActivity.class));
    }
}