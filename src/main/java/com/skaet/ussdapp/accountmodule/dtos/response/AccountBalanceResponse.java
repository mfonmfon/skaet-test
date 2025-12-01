package com.skaet.ussdapp.accountmodule.dtos.response;

import java.math.BigDecimal;

public class AccountBalanceResponse {
    private String accountNumber;
    private BigDecimal balance;
    private String accountHolderName;

    public AccountBalanceResponse() {
    }

    public AccountBalanceResponse(String accountNumber, BigDecimal balance, String accountHolderName) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountHolderName = accountHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }
}
