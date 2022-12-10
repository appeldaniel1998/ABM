package com.example.abm.Appointments.AppointmentCalendar;

import static com.example.abm.Appointments.AppointmentCalendar.CalendarUtils.daysInWeekArray;
import static com.example.abm.Appointments.AppointmentCalendar.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abm.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewActivity extends AppCompatActivity  implements CalendarAdapter.OnItemListener{
//Activity_appointments_calender_week_view
//A lot of the functions here are adapted from MainActivityTry
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_calender_week_view);
        initWidgets();
        setWeekView();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);

    }

    private void setWeekView()//Same as setMonthView
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);// we're going to have 7 colunms in our recycle view
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();

    }



    public void previousWeekAction(View view)//Same as previous month action
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view)//Same as previousWeekAction just different in plusWeeks
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
            CalendarUtils.selectedDate=date;
            setWeekView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setEventAdapter();

    }

    private void setEventAdapter()
    {
        ArrayList<Event> dailyEvents =Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(),dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(this,EventEditActivity.class));
    }
}