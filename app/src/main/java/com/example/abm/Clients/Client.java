package com.example.abm.Clients;

public class Client {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String birthdayDate;
    private String UID;
    private boolean isManager;

    /**
     * Default constructor for the class
     */
    public Client() {
    }

    /**
     * Constructor to be called while registering the user. All users registered are not managers
     *
     * @param firstName    first name
     * @param lastName     last name
     * @param email        email
     * @param phoneNumber  phone number
     * @param address      address
     * @param birthdayDate birthdayDate
     */
    public Client(String firstName, String lastName, String email, String phoneNumber, String address, String birthdayDate, String uid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthdayDate = birthdayDate;
        this.UID = uid;
        this.isManager = false;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthdayDate() {
        return birthdayDate;
    }

    public boolean isManager() {
        return isManager;
    }

    public String getUID() {
        return UID;
    }
}
