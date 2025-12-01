package com.skaet.ussdapp.accountmodule.dtos.response;

import java.math.BigDecimal;

public class DepositResponse {
    private String transactionReference;
    private BigDecimal amount;
    private BigDecimal newBalance;
    private String message;

    public DepositResponse() {
    }


    public DepositResponse(String transactionReference, BigDecimal amount, BigDecimal newBalance, String message) {
        this.transactionReference = transactionReference;
        this.amount = amount;
        this.newBalance = newBalance;
        this.message = message;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
