package com.example.abm.Appointments;

public class Appointment {
    private String appointmentID;
    private String date;
    private double startTime;
    private String typeName;
    private String clientId;


    public Appointment() {
    }

    public Appointment(String appointmentID, String date, double startTime, String typeName, String clientId) {
        this.appointmentID = appointmentID;
        this.date = date;
        this.startTime = startTime;
        this.typeName = typeName;
        this.clientId = clientId;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public String getDate() {
        return date;
    }

    public double getStartTime() {
        return startTime;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getClientId() {
        return clientId;
    }
}
