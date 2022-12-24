package com.example.abm.AppointmentCalendar;

import com.example.abm.Clients.Client;
import com.example.abm.HistoryAnalytics.HistoryActivity;
import com.example.abm.HistoryAnalytics.ClientActivities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event implements ClientActivities {
    public static ArrayList<Event> eventsList = new ArrayList<>(); //Define new array list of event

    private String appointmentId;
    private String appointmentType;
    private String clientName;
    private String clientId;

    private int date;
    private String startTime;

    public Event(){}

    //constructors
    public Event(String appointmentId, String typeName, String clientName, int date, String time,String ID) {
        this.appointmentId = appointmentId;
        this.appointmentType = typeName;
        this.date = date;
        this.startTime = time;
        this.clientName = clientName;
        this.clientId = ID;

    }

    //Getters and setters
    public String getAppointmentId() {
        return appointmentId;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientName() {
        Client currClient = CalendarMainActivity.clients.get(this.clientId);
        assert currClient != null;
        return currClient.getFirstName() + " " + currClient.getLastName();
//        return clientName
    }


    @Override
    public String getActivityName() {
        return appointmentType;
    }

    @Override
    public int getDate() {
        return date;
    }

    @Override
    public String getTime() {
        return this.startTime;
    }

    @Override
    public String getPrice() {
        try {
            return HistoryActivity.appointmentTypes.get(this.appointmentType).getPrice();
        }
        catch (Exception e) {
            return "-1";
        }
    }

    public String getStartTime() {
        return startTime;
    }

    //Function return all events for a given date
    public static ArrayList<Event> eventsForDate(LocalDate date) {
        int dateAsInt = localDateToInt(date);
        ArrayList<Event> events = new ArrayList<>(); //Define new array list of events
        for (Event event : eventsList) {
            if (event.getDate() == dateAsInt)//if the event is equal to the event thats passed in
            {
                events.add(event);//we will add it to our events list
            }
        }
        return events;
    }
    //function get appointment ID and return the Event
    public static Event getEvent(String appID)
    {
        Event specificEvent=new Event();
        for (Event event : eventsList) {
            if (event.getAppointmentId().equals(appID)) //if the eventID is equal to the eventID thats passed in
            {
                specificEvent= event;
            }
        }
        return specificEvent;
    }

    public static int localDateToInt(LocalDate localDate) {
        String[] arr = localDate.toString().split("-");
        String strDate = arr[0] + "" + arr[1] + "" + arr[2];
        return Integer.parseInt(strDate);
    }

    public static String timeIntToString(String time) {
        String hours = formatTime(Integer.parseInt(time) / 100);
        String minutes = formatTime(Integer.parseInt(time) % 100);
        return hours + ":" + minutes;
    }

    private static String formatTime(int timePart) {
        if (timePart < 10) {
            return "0" + timePart;
        }
        return timePart + "";
    }

    public static String timeStringToInt(String time) {
        String timeClean = "";
        for (int i = 6; i < time.length(); i++) {
            timeClean += time.charAt(i);
        }
        String[] timeSplit = timeClean.split(":");
        return timeSplit[0] + "" + timeSplit[1];
    }

    public static int localTimeToInt(LocalTime localTime) {
        String[] splitTime = localTime.toString().split(":");
        String relevantTime = splitTime[0] + "" + splitTime[1];
        return Integer.parseInt(relevantTime);
    }
}
