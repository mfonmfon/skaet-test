package com.skaet.ussdapp.transactionmodule.dtos.response;

import com.skaet.ussdapp.transactionmodule.TransactionEntity;

import java.util.List;

public class TransactionHistoryResponse {
    private List<TransactionEntity> transactions;

    public TransactionHistoryResponse() {
    }

    public TransactionHistoryResponse(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }
}
