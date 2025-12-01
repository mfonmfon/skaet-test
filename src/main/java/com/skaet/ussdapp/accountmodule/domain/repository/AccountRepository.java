package com.skaet.ussdapp.accountmodule.domain.repository;

import com.skaet.ussdapp.accountmodule.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByUserId(Long userId);
    Optional<Account> findByUser_PhoneNumber(String phoneNumber);
}
