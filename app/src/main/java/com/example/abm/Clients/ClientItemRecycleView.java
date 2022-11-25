package com.example.abm.Clients;

public class ClientItemRecycleView {
    private String name;
    private String email;
    private String UID;

    public ClientItemRecycleView(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.UID = uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUID() {
        return UID;
    }
}
