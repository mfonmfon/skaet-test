package com.skaet.ussdapp.ussdmodule.service.interfaces;

import com.skaet.ussdapp.ussdmodule.domain.model.UssdSessionEntity;

import java.util.Optional;

public interface UssdSessionService {
    UssdSessionEntity createOrUpdateSession(String sessionId, String phoneNumber, String currentMenu, String sessionData);
    Optional<UssdSessionEntity> getSession(String sessionId);
    void deleteSession(String sessionId);
    void cleanupExpiredSessions();
}
