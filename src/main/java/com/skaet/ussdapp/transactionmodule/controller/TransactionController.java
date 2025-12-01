package com.skaet.ussdapp.transactionmodule.controller;

import com.skaet.ussdapp.constant.ResponseMessages;
import com.skaet.ussdapp.common.dto.ApiResponse;
import com.skaet.ussdapp.transactionmodule.dtos.response.TransactionHistoryResponse;
import com.skaet.ussdapp.transactionmodule.service.interfaces.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<TransactionHistoryResponse>> getTransactionHistory(@RequestParam String accountNumber) {
        TransactionHistoryResponse historyResponse = transactionService.getTransactionHistory(accountNumber);
        
        ApiResponse<TransactionHistoryResponse> response = ApiResponse.<TransactionHistoryResponse>builder()
                .data(historyResponse)
                .isSuccessful(true)
                .message(ResponseMessages.TRANSACTION_HISTORY_RETRIEVED.getMessage())
                .status(HttpStatus.OK.value())
                .build();
        
        return ResponseEntity.ok(response);
    }
}
