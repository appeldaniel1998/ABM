package com.example.abm.Appointments;

public class Appointment {
    private String appointmentId;
    private int date;//
    private String startTime;//
    private String appointmentType;//
    private String clientId;


    public Appointment() {
    }

    public Appointment(String appointmentID, int date, String startTime, String appointmentType, String clientId) {
        this.appointmentId = appointmentID;
        this.date = date;
        this.startTime = startTime;
        this.appointmentType = appointmentType;
        this.clientId = clientId;
    }

    public String getAppointmentID() {
        return appointmentId;
    }

    public int getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public String getClientId() {
        return clientId;
    }

    //toString method for debugging
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", appointmentType='" + appointmentType + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
