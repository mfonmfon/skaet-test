package com.skaet.ussdapp.ussdmodule.dtos.request;

public class UssdRequest {
    private String sessionId;
    private String phoneNumber;
    private String text;
    private String serviceCode;

    public UssdRequest() {
    }

    public UssdRequest(String sessionId, String phoneNumber, String text, String serviceCode) {
        this.sessionId = sessionId;
        this.phoneNumber = phoneNumber;
        this.text = text;
        this.serviceCode = serviceCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
