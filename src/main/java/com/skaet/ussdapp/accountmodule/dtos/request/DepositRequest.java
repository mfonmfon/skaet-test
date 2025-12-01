package com.skaet.ussdapp.accountmodule.dtos.request;

import java.math.BigDecimal;

public class DepositRequest {
    private String phoneNumber;
    private BigDecimal amount;

    public DepositRequest() {
    }

    public DepositRequest(String phoneNumber, BigDecimal amount) {
        this.phoneNumber = phoneNumber;
        this.amount = amount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
