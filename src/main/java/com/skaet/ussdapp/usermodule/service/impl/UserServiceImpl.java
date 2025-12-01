package com.skaet.ussdapp.usermodule.service.impl;

import com.skaet.ussdapp.accountmodule.domain.model.Account;
import com.skaet.ussdapp.accountmodule.domain.repository.AccountRepository;
import com.skaet.ussdapp.common.exception.UserAlreadyExistsException;
import com.skaet.ussdapp.constant.AccountStatus;
import com.skaet.ussdapp.usermodule.domain.model.UserEntity;
import com.skaet.ussdapp.usermodule.domain.repository.UserRepository;
import com.skaet.ussdapp.usermodule.dtos.request.CreateUserAccountRequest;
import com.skaet.ussdapp.usermodule.dtos.response.CreateUserAccountResponse;
import com.skaet.ussdapp.usermodule.service.interfaces.UserService;
import com.skaet.ussdapp.walletmodule.domain.model.WalletEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserServiceImpl(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public CreateUserAccountResponse createAccount(CreateUserAccountRequest request) {
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new UserAlreadyExistsException("User with phone number already exists");
        }

        if (request.getPin().length() != 4 || !request.getPin().matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits");
        }

        UserEntity user = new UserEntity();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPinHash(request.getPin()); // TODO: Hash PIN in production
        user.setCreatedAt(LocalDateTime.now());

        WalletEntity wallet = new WalletEntity();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setUser(user);
        user.setWallet(wallet);

        UserEntity savedUser = userRepository.save(user);

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setUser(savedUser);
        account.setCreatedAt(LocalDateTime.now());

        Account savedAccount = accountRepository.save(account);

        return new CreateUserAccountResponse(
                savedUser.getId(),
                savedUser.getPhoneNumber(),
                savedAccount.getAccountNumber(),
                "Account created successfully"
        );
    }

    private String generateAccountNumber() {
        Random random = new Random();
        long number = 1000000000L + (long) (random.nextDouble() * 9000000000L);
        return String.valueOf(number);
    }
}
