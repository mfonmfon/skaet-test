package com.skaet.ussdapp.accountmodule.service.interfaces;

import com.skaet.ussdapp.accountmodule.dtos.request.DepositRequest;
import com.skaet.ussdapp.accountmodule.dtos.request.WithdrawalRequest;
import com.skaet.ussdapp.accountmodule.dtos.response.AccountBalanceResponse;
import com.skaet.ussdapp.accountmodule.dtos.response.DepositResponse;
import com.skaet.ussdapp.accountmodule.dtos.response.WithdrawalResponse;

public interface AccountService {
    DepositResponse deposit(DepositRequest depositRequest);
    WithdrawalResponse withdraw(WithdrawalRequest withdrawalRequest);
    AccountBalanceResponse getBalance(String phoneNumber);
}
