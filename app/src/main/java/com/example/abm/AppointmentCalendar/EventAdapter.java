package com.example.abm.AppointmentCalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.abm.Clients.Client;
import com.example.abm.R;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
        //constructor
    public EventAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);

        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_appointments_calender_event_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);//view of event in event list (part of recycle view display)

        Client currClient = CalendarMainActivity.clients.get(event.getClientId());
        //get details to represent it in wee view as list of all events
        String eventTitle = event.getAppointmentType() +":"+ currClient.getFirstName() + " " + currClient.getLastName() +": " + Event.timeIntToString(event.getStartTime());
        eventCellTV.setText(eventTitle);
        return convertView;
    }
}