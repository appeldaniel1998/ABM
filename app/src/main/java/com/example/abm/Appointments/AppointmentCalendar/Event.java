package com.example.abm.Appointments.AppointmentCalendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event
{
    public static ArrayList<Event> eventsList=new ArrayList<>(); //Define new array list of events

    //Function return all events for a given date
    public static ArrayList<Event> eventsForDate (LocalDate date)
    {
        ArrayList<Event> events=new ArrayList<>(); //Define new array list of events
        for (Event event : eventsList)
        {
            if(event.getDate().equals(date))//if the event is equal to the event thats passed in
            {
                events.add(event);//we will add it to our events list
            }
        }
        return events;
    }

    private String name;
    private LocalDate date;
    private LocalTime time;


    //constructors
    public Event(String name, LocalDate date, LocalTime time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }
//Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
