package com.skaet.ussdapp.ussdmodule.dtos.response;

public class UssdResponse {
    private String message;
    private boolean continueSession;

    public UssdResponse(String message, boolean continueSession) {
        this.message = message;
        this.continueSession = continueSession;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isContinueSession() {
        return continueSession;
    }

    public void setContinueSession(boolean continueSession) {
        this.continueSession = continueSession;
    }
}
