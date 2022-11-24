package com.example.abm.Clients;

public class ClientItemRecycleView {
    private String name;
    private String email;

    public ClientItemRecycleView(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
