package com.example.abm.Appointments.AppointmentCalendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {
    public static ArrayList<Event> eventsList = new ArrayList<>(); //Define new array list of event

    private String appointmentId;
    private String appointmentType;
    private String clientId;
    private int date;
    private String startTime;

    public Event(){}

    //constructors
    public Event(String appointmentId, String typeName, String clientName, int date, String time) {
        this.appointmentId = appointmentId;
        this.appointmentType = typeName;
        this.date = date;
        this.startTime = time;
        this.clientId = clientName;
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

    public int getDate() {
        return date;
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
