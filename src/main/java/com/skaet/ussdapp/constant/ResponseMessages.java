package com.skaet.ussdapp.constant;

public enum ResponseMessages {
    // User messages
    USER_REGISTRATION_SUCCESS("User registered successfully"),
    USER_ALREADY_EXISTS("User with this phone number already exists"),
    USER_NOT_FOUND("User not found"),
    
    // Account messages
    ACCOUNT_CREATED_SUCCESS("Account created successfully"),
    ACCOUNT_NOT_FOUND("Account not found"),
    BALANCE_RETRIEVED_SUCCESS("Balance retrieved successfully"),
    
    // Transaction messages
    DEPOSIT_SUCCESS("Deposit completed successfully"),
    WITHDRAWAL_SUCCESS("Withdrawal completed successfully"),
    INSUFFICIENT_BALANCE("Insufficient balance for this transaction"),
    INVALID_AMOUNT("Amount must be greater than zero"),
    TRANSACTION_HISTORY_RETRIEVED("Transaction history retrieved successfully"),
    
    // USSD messages
    USSD_REQUEST_PROCESSED("USSD request processed successfully"),
    SESSION_EXPIRED("Session has expired. Please try again"),
    INVALID_INPUT("Invalid input provided"),
    
    // General messages
    OPERATION_SUCCESS("Operation completed successfully"),
    OPERATION_FAILED("Operation failed"),
    INTERNAL_SERVER_ERROR("An unexpected error occurred");

    private final String message;

    ResponseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
