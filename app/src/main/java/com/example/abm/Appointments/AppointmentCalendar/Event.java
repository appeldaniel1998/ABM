package com.example.abm.Appointments.AppointmentCalendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event
{
    public static ArrayList<Event> eventsList=new ArrayList<>(); //Define new array list of event

    private String appointmentId;
    private String typeName;
    private int date;
    private int startTime;
    private String clientId;


    //constructors
    public Event(String typeName, int date, int time) {
        this.typeName = typeName;
        this.date = date;
        this.startTime = time;
    }

    //Getters and setters
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getClientId() {
        return clientId;
    }

    //Function return all events for a given date
    public static ArrayList<Event> eventsForDate (LocalDate date)
    {
        int dateAsInt = localDateToInt(date);
        ArrayList<Event> events=new ArrayList<>(); //Define new array list of events
        for (Event event : eventsList)
        {
            if(event.getDate() == dateAsInt)//if the event is equal to the event thats passed in
            {
                events.add(event);//we will add it to our events list
            }
        }
        return events;
    }

    public static int localDateToInt(LocalDate localDate) {
        String[] arr = localDate.toString().split("-");
        String strDate = arr[0] + "" + arr[1] + "" + arr[2];
        return Integer.parseInt(strDate);
    }

    public static String timeIntToString(int time) {
        int hours = time / 100;
        int minutes = time % 100;
        return hours + ":" + minutes;
    }

    public static int localTimeToInt(LocalTime localTime) {
        String[] splitTime = localTime.toString().split(":");
        String relevantTime = splitTime[0] + "" + splitTime[1];
        return Integer.parseInt(relevantTime);
    }
}
