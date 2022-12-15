package com.example.abm.AppointmentType;

public class AppointmentType {
    private String typeName;
    private String price;
    private String duration;

    public AppointmentType() {

    }

    public AppointmentType(String typeName, String price, String duration) {
        this.typeName = typeName;
        this.price = price;
        this.duration = duration;
    }


    public String getTypeName() {
        return typeName;
    }

    public String getPrice() {
        return price;
    }

    public String getDuration() {
        return duration;
    }
}
