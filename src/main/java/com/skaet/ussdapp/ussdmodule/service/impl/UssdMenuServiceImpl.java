package com.skaet.ussdapp.ussdmodule.service.impl;

import com.skaet.ussdapp.accountmodule.dtos.request.DepositRequest;
import com.skaet.ussdapp.accountmodule.dtos.request.WithdrawalRequest;
import com.skaet.ussdapp.accountmodule.dtos.response.AccountBalanceResponse;
import com.skaet.ussdapp.accountmodule.dtos.response.DepositResponse;
import com.skaet.ussdapp.accountmodule.dtos.response.WithdrawalResponse;
import com.skaet.ussdapp.accountmodule.service.interfaces.AccountService;
import com.skaet.ussdapp.transactionmodule.dtos.response.TransactionHistoryResponse;
import com.skaet.ussdapp.transactionmodule.service.interfaces.TransactionService;
import com.skaet.ussdapp.usermodule.domain.repository.UserRepository;
import com.skaet.ussdapp.usermodule.dtos.request.CreateUserAccountRequest;
import com.skaet.ussdapp.usermodule.dtos.response.CreateUserAccountResponse;
import com.skaet.ussdapp.usermodule.service.interfaces.UserService;
import com.skaet.ussdapp.ussdmodule.domain.model.UssdSessionEntity;
import com.skaet.ussdapp.ussdmodule.dtos.request.UssdRequest;
import com.skaet.ussdapp.ussdmodule.dtos.response.UssdResponse;
import com.skaet.ussdapp.ussdmodule.service.interfaces.UssdMenuService;
import com.skaet.ussdapp.ussdmodule.service.interfaces.UssdSessionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UssdMenuServiceImpl implements UssdMenuService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final UssdSessionService sessionService;

    public UssdMenuServiceImpl(UserService userService, UserRepository userRepository,
                               AccountService accountService, TransactionService transactionService,
                               UssdSessionService sessionService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.sessionService = sessionService;
    }

    @Override
    public UssdResponse processUssdRequest(UssdRequest request) {
        try {
            String phoneNumber = request.getPhoneNumber();
            String text = request.getText() == null ? "" : request.getText().trim();
            String sessionId = request.getSessionId();

            // Handle empty text
            String[] inputs;
            int level;
            
            if (text == null || text.isEmpty()) {
                inputs = new String[]{""};
                level = 1;
            } else {
                inputs = text.split("\\*");
                level = inputs.length;
            }

            // Show welcome screen for new users
            if (!userRepository.existsByPhoneNumber(phoneNumber)) {
                return handleNewUserWelcome(sessionId, phoneNumber, text, inputs, level);
            } else {
                return handleExistingUserMenu(sessionId, phoneNumber, text, inputs, level);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return new UssdResponse("Invalid input. Please try again.", false);
        } catch (Exception e) {
            sessionService.deleteSession(request.getSessionId());
            return new UssdResponse("An error occurred: " + e.getMessage(), false);
        }
    }

    private UssdResponse handleNewUserWelcome(String sessionId, String phoneNumber, String text, String[] inputs, int level) {
        // Level 1: Show welcome and option to create account
        if (text == null || text.isEmpty() || inputs.length == 0 || inputs[0].isEmpty()) {
            return new UssdResponse(
                    "Welcome to Mobile Banking!\n\n" +
                            "You don't have an account yet.\n" +
                            "1. Create Account\n" +
                            "0. Exit",
                    true
            );
        }

        String choice = inputs[0];

        // User chose to create account
        if ("1".equals(choice)) {
            return handleRegistration(sessionId, phoneNumber, text, inputs, level);
        }

        // User chose to exit
        if ("0".equals(choice)) {
            return new UssdResponse("Thank you for using Mobile Banking. Goodbye!", false);
        }

        return new UssdResponse("Invalid option. Please try again.", false);
    }

    private UssdResponse handleRegistration(String sessionId, String phoneNumber, String text, String[] inputs, int level) {
        // Level 2: Ask for first name
        if (level == 1) {
            return new UssdResponse("Enter your first name:", true);
        }

        // Level 3: Ask for last name
        if (level == 2 && inputs.length >= 2) {
            String firstName = inputs[1];
            if (firstName.isEmpty()) {
                return new UssdResponse("First name cannot be empty.\nEnter your first name:", true);
            }
            sessionService.createOrUpdateSession(sessionId, phoneNumber, "REGISTRATION_FIRST_NAME", firstName);
            return new UssdResponse("Enter your last name:", true);
        }

        // Level 4: Ask for PIN
        if (level == 3 && inputs.length >= 3) {
            Optional<UssdSessionEntity> session = sessionService.getSession(sessionId);
            if (session.isEmpty()) {
                return new UssdResponse("Session expired. Please try again.", false);
            }

            String firstName = session.get().getSessionData();
            String lastName = inputs[2];
            if (lastName.isEmpty()) {
                return new UssdResponse("Last name cannot be empty.\nEnter your last name:", true);
            }
            String combinedNames = firstName + "|" + lastName;
            sessionService.createOrUpdateSession(sessionId, phoneNumber, "REGISTRATION_LAST_NAME", combinedNames);
            return new UssdResponse("Enter a 4-digit PIN:", true);
        }

        // Level 5: Complete registration and show main menu
        if (level == 4 && inputs.length >= 4) {
            Optional<UssdSessionEntity> session = sessionService.getSession(sessionId);
            if (session.isEmpty()) {
                return new UssdResponse("Session expired. Please try again.", false);
            }

            String[] names = session.get().getSessionData().split("\\|");
            if (names.length < 2) {
                return new UssdResponse("Session error. Please try again.", false);
            }
            
            String firstName = names[0];
            String lastName = names[1];
            String pin = inputs[3];

            CreateUserAccountRequest createRequest = new CreateUserAccountRequest(firstName, lastName, phoneNumber, pin);
            CreateUserAccountResponse response = userService.createAccount(createRequest);

            sessionService.deleteSession(sessionId);
            
            // Show success and main menu
            return new UssdResponse(
                    "Registration successful!\n" +
                            "Account: " + response.getAccountNumber() + "\n\n" +
                            "What would you like to do?\n" +
                            "1. Check Balance\n" +
                            "2. Deposit\n" +
                            "3. Withdraw\n" +
                            "4. Transaction History\n" +
                            "0. Exit",
                    true
            );
        }

        return new UssdResponse("Invalid input. Please try again.", false);
    }

    private UssdResponse handleExistingUserMenu(String sessionId, String phoneNumber, String text, String[] inputs, int level) {
        // Show main menu if text is empty or no inputs
        if (text.isEmpty() || inputs.length == 0 || inputs[0].isEmpty()) {
            return new UssdResponse(
                    "Welcome to Mobile Banking!\n\n" +
                            "What would you like to do?\n" +
                            "1. Check Balance\n" +
                            "2. Deposit\n" +
                            "3. Withdraw\n" +
                            "4. Transaction History\n" +
                            "0. Exit",
                    true
            );
        }

        String choice = inputs[0];

        return switch (choice) {
            case "1" -> handleCheckBalance(sessionId, phoneNumber, inputs, level);
            case "2" -> handleDeposit(sessionId, phoneNumber, inputs, level);
            case "3" -> handleWithdrawal(sessionId, phoneNumber, inputs, level);
            case "4" -> handleTransactionHistory(sessionId, phoneNumber, inputs, level);
            case "0" -> new UssdResponse("Thank you for using Mobile Banking. Goodbye!", false);
            default -> new UssdResponse("Invalid option. Please try again.", false);
        };
    }

    private UssdResponse handleCheckBalance(String sessionId, String phoneNumber, String[] inputs, int level) {
        AccountBalanceResponse balance = accountService.getBalance(phoneNumber);
        return new UssdResponse(
                "Account: " + balance.getAccountNumber() + "\n" +
                        "Balance: $" + balance.getBalance() + "\n" +
                        "Name: " + balance.getAccountHolderName() + "\n\n" +
                        "What would you like to do?\n" +
                        "1. Check Balance\n" +
                        "2. Deposit\n" +
                        "3. Withdraw\n" +
                        "4. Transaction History\n" +
                        "0. Exit",
                true
        );
    }

    private UssdResponse handleDeposit(String sessionId, String phoneNumber, String[] inputs, int level) {
        // Ask for amount
        if (level == 1) {
            return new UssdResponse("Enter amount to deposit:", true);
        }

        // Process deposit and show menu again
        if (level == 2 && inputs.length >= 2) {
            try {
                BigDecimal amount = new BigDecimal(inputs[1]);
                DepositRequest depositRequest = new DepositRequest(phoneNumber, amount);
                DepositResponse response = accountService.deposit(depositRequest);

                return new UssdResponse(
                        "Deposit successful!\n" +
                                "Amount: $" + response.getAmount() + "\n" +
                                "New Balance: $" + response.getNewBalance() + "\n" +
                                "Ref: " + response.getTransactionReference() + "\n\n" +
                                "What would you like to do?\n" +
                                "1. Check Balance\n" +
                                "2. Deposit\n" +
                                "3. Withdraw\n" +
                                "4. Transaction History\n" +
                                "0. Exit",
                        true
                );
            } catch (NumberFormatException e) {
                return new UssdResponse("Invalid amount. Please enter a valid number.", false);
            } catch (Exception e) {
                return new UssdResponse("Deposit failed: " + e.getMessage(), false);
            }
        }

        return new UssdResponse("Invalid input.", false);
    }

    private UssdResponse handleWithdrawal(String sessionId, String phoneNumber, String[] inputs, int level) {
        // Ask for amount
        if (level == 1) {
            return new UssdResponse("Enter amount to withdraw:", true);
        }
        
        // Process withdrawal and show menu again
        if (level == 2 && inputs.length >= 2) {
            try {
                BigDecimal amount = new BigDecimal(inputs[1]);
                WithdrawalRequest withdrawalRequest = new WithdrawalRequest(phoneNumber, amount);
                WithdrawalResponse response = accountService.withdraw(withdrawalRequest);

                return new UssdResponse(
                        "Withdrawal successful!\n" +
                                "Amount: $" + response.getAmount() + "\n" +
                                "New Balance: $" + response.getNewBalance() + "\n" +
                                "Ref: " + response.getTransactionReference() + "\n\n" +
                                "What would you like to do?\n" +
                                "1. Check Balance\n" +
                                "2. Deposit\n" +
                                "3. Withdraw\n" +
                                "4. Transaction History\n" +
                                "0. Exit",
                        true
                );
            } catch (NumberFormatException e) {
                return new UssdResponse("Invalid amount. Please enter a valid number.", false);
            } catch (Exception e) {
                return new UssdResponse("Withdrawal failed: " + e.getMessage(), false);
            }
        }

        return new UssdResponse("Invalid input.", false);
    }

    private UssdResponse handleTransactionHistory(String sessionId, String phoneNumber, String[] inputs, int level) {
        TransactionHistoryResponse history = transactionService.getTransactionHistory(phoneNumber);

        StringBuilder message = new StringBuilder();
        
        if (history.getTransactions().isEmpty()) {
            message.append("No transactions found.\n\n");
        } else {
            message.append("Last 5 Transactions:\n");
            history.getTransactions().stream().limit(5).forEach(txn -> {
                message.append(txn.getTransactionType()).append(" $").append(txn.getAmount())
                        .append(" - ").append(txn.getCreatedAt().toLocalDate()).append("\n");
            });
            message.append("\n");
        }

        message.append("What would you like to do?\n")
                .append("1. Check Balance\n")
                .append("2. Deposit\n")
                .append("3. Withdraw\n")
                .append("4. Transaction History\n")
                .append("0. Exit");

        return new UssdResponse(message.toString(), true);
    }
}
