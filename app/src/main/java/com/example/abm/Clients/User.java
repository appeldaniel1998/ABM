package com.example.abm.Clients;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String birthdayDate;
    private boolean isManager;

    public User() {
    };

    /**
     * Constructor to be called while registering the user. All users registered are not managers
     *
     * @param firstName      first name
     * @param lastName       last name
     * @param email          email
     * @param phoneNumber    phone number
     * @param address        address
     * @param birthdayDate   birthdayDate
     */
    public User(String firstName, String lastName, String email, String phoneNumber, String address, String birthdayDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthdayDate = birthdayDate;
        this.isManager = false;
    }

    /**
     * Constructor to be called when a manager is manually registering a new user, with the option of making him/her a manager.
     *
     * @param firstName      first name
     * @param lastName       last name
     * @param email          email
     * @param phoneNumber    phone number
     * @param address        address
     * @param birthdayDate   birthdayDate
     * @param isManager      if the new user is a manager
     */
    public User(String firstName, String lastName, String email, String phoneNumber, String address, String birthdayDate, boolean isManager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthdayDate = birthdayDate;
        this.isManager = isManager;
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
}
