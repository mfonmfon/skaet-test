package com.skaet.ussdapp.ussdmodule.domain.repository;

import com.skaet.ussdapp.ussdmodule.domain.model.UssdSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UssdSessionRepository extends JpaRepository<UssdSessionEntity, Long> {
    Optional<UssdSessionEntity> findBySessionId(String sessionId);
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
