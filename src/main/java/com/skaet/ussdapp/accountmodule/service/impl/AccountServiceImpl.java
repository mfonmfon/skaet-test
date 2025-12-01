package com.skaet.ussdapp.accountmodule.service.impl;

import com.skaet.ussdapp.accountmodule.domain.model.Account;
import com.skaet.ussdapp.accountmodule.domain.repository.AccountRepository;
import com.skaet.ussdapp.accountmodule.dtos.request.DepositRequest;
import com.skaet.ussdapp.accountmodule.dtos.request.WithdrawalRequest;
import com.skaet.ussdapp.accountmodule.dtos.response.AccountBalanceResponse;
import com.skaet.ussdapp.accountmodule.dtos.response.DepositResponse;
import com.skaet.ussdapp.accountmodule.dtos.response.WithdrawalResponse;
import com.skaet.ussdapp.accountmodule.service.interfaces.AccountService;
import com.skaet.ussdapp.common.exception.AccountNotFoundException;
import com.skaet.ussdapp.common.exception.InsufficientBalanceException;
import com.skaet.ussdapp.constant.TransactionStausCode;
import com.skaet.ussdapp.constant.TransactionType;
import com.skaet.ussdapp.transactionmodule.TransactionEntity;
import com.skaet.ussdapp.transactionmodule.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public DepositResponse deposit(DepositRequest depositRequest) {
        if (depositRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        Account account = findAccountByPhoneNumber(depositRequest.getPhoneNumber());
        BigDecimal oldBalance = account.getBalance();
        account.setBalance(oldBalance.add(depositRequest.getAmount()));
        accountRepository.save(account);

        String reference = generateTransactionReference();
        recordTransaction(account.getId(), TransactionType.DEPOSIT, depositRequest.getAmount(), reference);

        return new DepositResponse(reference, depositRequest.getAmount(), account.getBalance(), "Deposit successful");
    }

    @Override
    @Transactional
    public WithdrawalResponse withdraw(WithdrawalRequest withdrawalRequest) {
        if (withdrawalRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }

        Account account = findAccountByPhoneNumber(withdrawalRequest.getPhoneNumber());

        if (account.getBalance().compareTo(withdrawalRequest.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        BigDecimal oldBalance = account.getBalance();
        account.setBalance(oldBalance.subtract(withdrawalRequest.getAmount()));
        accountRepository.save(account);

        String reference = generateTransactionReference();
        recordTransaction(account.getId(), TransactionType.WITHDRAWAL, withdrawalRequest.getAmount(), reference);

        return new WithdrawalResponse(reference, withdrawalRequest.getAmount(), account.getBalance(), "Withdrawal successful");
    }

    @Override
    public AccountBalanceResponse getBalance(String phoneNumber) {
        Account account = findAccountByPhoneNumber(phoneNumber);

        String fullName = account.getUser().getFirstName() + " " + account.getUser().getLastName();

        return new AccountBalanceResponse(account.getAccountNumber(), account.getBalance(), fullName);
    }

    private Account findAccountByPhoneNumber(String phoneNumber) {
        return accountRepository.findByUser_PhoneNumber(phoneNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found for phone number"));
    }

    private void recordTransaction(Long accountId, TransactionType type, BigDecimal amount, String reference) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setAccountId(accountId);
        transaction.setTransactionType(type);
        transaction.setAmount(amount);
        transaction.setReference(reference);
        transaction.setStausCode(TransactionStausCode.SUCCESS);
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);
    }

    private String generateTransactionReference() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
