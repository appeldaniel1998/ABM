package com.example.abm.AppointmentCalendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abm.R;

import java.time.LocalDate;
import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    //Constructor
    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener)
    {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    //Define the size of the calendar in month view
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //define the connection to calender_cell.xml
        View view = inflater.inflate(R.layout.activity_appointments_calender_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (days.size()>15){//month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        }
        else{//week view
            layoutParams.height = (int) parent.getHeight();

        }
        return new CalendarViewHolder( view, onItemListener, days);//return the cakendar view
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {//function fill up the calendar month view (i.e empty cells and not empty cells)
        final LocalDate date = days.get(position);
        if(date==null)//if day is null we will define it as empty string
        {
            holder.dayOfMonth.setText(" ");//empty cell
        }
        else//if day is not null than we are going to set it to out date
        {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));//cell with date inside
            if(date.equals(CalendarUtils.selectedDate))
            {
                holder.parentView.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    @Override
    //function count how many days
    public int getItemCount()
    {
        return days.size();
    }

    //to create onclick listener
    public interface  OnItemListener
    {
        void onItemClick(int position, LocalDate date);
    }
}