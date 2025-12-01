package com.skaet.ussdapp.transactionmodule.service.impl;
import com.skaet.ussdapp.accountmodule.domain.model.Account;
import com.skaet.ussdapp.accountmodule.domain.repository.AccountRepository;
import com.skaet.ussdapp.common.exception.AccountNotFoundException;
import com.skaet.ussdapp.transactionmodule.TransactionEntity;
import com.skaet.ussdapp.transactionmodule.domain.repository.TransactionRepository;
import com.skaet.ussdapp.transactionmodule.dtos.response.TransactionHistoryResponse;
import com.skaet.ussdapp.transactionmodule.service.interfaces.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public TransactionHistoryResponse getTransactionHistory(String accountNumber) {
       Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        List<TransactionEntity> transactions = transactionRepository.findByAccountIdOrderByCreatedAtDesc(account.getId());

        return new TransactionHistoryResponse(transactions);
    }
}
