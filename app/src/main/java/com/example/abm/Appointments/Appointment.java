package com.example.abm.Appointments;

public class Appointment {
    private String appointmentId;
    private String date;
    private String startTime;
    private String typeName;
    private String clientId;


    public Appointment() {
    }

    public Appointment(String appointmentID, String date, String startTime, String typeName, String clientId) {
        this.appointmentId = appointmentID;
        this.date = date;
        this.startTime = startTime;
        this.typeName = typeName;
        this.clientId = clientId;
    }

    public String getAppointmentID() {
        return appointmentId;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getClientId() {
        return clientId;
    }
}
