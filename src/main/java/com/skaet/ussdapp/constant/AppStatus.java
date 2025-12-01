package com.skaet.ussdapp.constant;

public enum AppStatus {

    USER_NOT_FOUND_EXCEPTION_MESSAGE("User not found"),
    USER_ALREADY_EXIST_EXCEPTION_MESSAGE("User already exist");

    private String message;

    AppStatus(String message){
        this.message=message;
    }

    public String getMessage() {
        return message;
    }
}
