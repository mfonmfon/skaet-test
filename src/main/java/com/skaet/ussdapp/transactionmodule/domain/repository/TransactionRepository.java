package com.skaet.ussdapp.transactionmodule.domain.repository;

import com.skaet.ussdapp.transactionmodule.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByAccountIdOrderByCreatedAtDesc(Long accountId);
}
