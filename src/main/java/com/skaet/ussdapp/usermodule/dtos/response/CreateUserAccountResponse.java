package com.skaet.ussdapp.usermodule.dtos.response;

public class CreateUserAccountResponse {
    private Long userId;
    private String phoneNumber;
    private String accountNumber;
    private String message;

    public CreateUserAccountResponse() {
    }

    public CreateUserAccountResponse(Long userId, String phoneNumber, String accountNumber, String message) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.accountNumber = accountNumber;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
