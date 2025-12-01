package com.skaet.ussdapp.usermodule.dtos.request;

public class CreateUserAccountRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String pin;

    public CreateUserAccountRequest() {
    }

    public CreateUserAccountRequest(String firstName, String lastName, String phoneNumber, String pin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.pin = pin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
