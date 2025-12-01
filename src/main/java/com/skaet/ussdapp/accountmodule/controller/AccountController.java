package com.skaet.ussdapp.accountmodule.controller;

import com.skaet.ussdapp.accountmodule.dtos.request.DepositRequest;
import com.skaet.ussdapp.accountmodule.dtos.request.WithdrawalRequest;
import com.skaet.ussdapp.accountmodule.dtos.response.AccountBalanceResponse;
import com.skaet.ussdapp.accountmodule.dtos.response.DepositResponse;
import com.skaet.ussdapp.accountmodule.dtos.response.WithdrawalResponse;
import com.skaet.ussdapp.accountmodule.service.interfaces.AccountService;
import com.skaet.ussdapp.common.constants.ResponseMessages;
import com.skaet.ussdapp.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<DepositResponse>> deposit(@RequestBody DepositRequest request) {
        DepositResponse depositResponse = accountService.deposit(request);
        
        ApiResponse<DepositResponse> response = ApiResponse.<DepositResponse>builder()
                .data(depositResponse)
                .isSuccessful(true)
                .message(ResponseMessages.DEPOSIT_SUCCESS.getMessage())
                .status(HttpStatus.OK.value())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<WithdrawalResponse>> withdraw(@RequestBody WithdrawalRequest request) {
        WithdrawalResponse withdrawalResponse = accountService.withdraw(request);
        
        ApiResponse<WithdrawalResponse> response = ApiResponse.<WithdrawalResponse>builder()
                .data(withdrawalResponse)
                .isSuccessful(true)
                .message(ResponseMessages.WITHDRAWAL_SUCCESS.getMessage())
                .status(HttpStatus.OK.value())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<AccountBalanceResponse>> getBalance(@RequestParam String phoneNumber) {
        AccountBalanceResponse balanceResponse = accountService.getBalance(phoneNumber);
        
        ApiResponse<AccountBalanceResponse> response = ApiResponse.<AccountBalanceResponse>builder()
                .data(balanceResponse)
                .isSuccessful(true)
                .message(ResponseMessages.BALANCE_RETRIEVED_SUCCESS.getMessage())
                .status(HttpStatus.OK.value())
                .build();
        
        return ResponseEntity.ok(response);
    }
}
