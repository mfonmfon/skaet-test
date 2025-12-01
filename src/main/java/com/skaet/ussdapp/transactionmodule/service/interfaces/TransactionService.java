package com.skaet.ussdapp.transactionmodule.service.interfaces;

import com.skaet.ussdapp.transactionmodule.dtos.response.TransactionHistoryResponse;

public interface TransactionService {
    TransactionHistoryResponse getTransactionHistory(String accountNumber);
}
