package com.example.abm.Appointments.AppointmentCalender;

import static com.example.abm.Appointments.AppointmentCalender.CalenderUtils.daysInMonthArray;
import static com.example.abm.Appointments.AppointmentCalender.CalenderUtils.daysInWeekArray;
import static com.example.abm.Appointments.AppointmentCalender.CalenderUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abm.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewActivity extends AppCompatActivity  implements CalendarAdapter.OnItemListener{
//Activity_appointments_calender_week_view
//A lot of the functions here are adapted from MainActivityTry
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

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
    }

    private void setWeekView()//Same as setMonthView
    {
        monthYearText.setText(monthYearFromDate(CalenderUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalenderUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);// we're going to have 7 colunms in our recycle view
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }



    public void previousWeekAction(View view)//Same as previous month action
    {
        CalenderUtils.selectedDate = CalenderUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view)//Same as previousWeekAction just different in plusWeeks
    {
        CalenderUtils.selectedDate = CalenderUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(CalenderUtils.selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    public void newEventAction(View view) {
    }
}