package com.skaet.ussdapp.ussdmodule.service.impl;

import com.skaet.ussdapp.ussdmodule.domain.model.UssdSessionEntity;
import com.skaet.ussdapp.ussdmodule.domain.repository.UssdSessionRepository;
import com.skaet.ussdapp.ussdmodule.service.interfaces.UssdSessionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UssdSessionServiceImpl implements UssdSessionService {

    private final UssdSessionRepository sessionRepository;

    public UssdSessionServiceImpl(UssdSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional
    public UssdSessionEntity createOrUpdateSession(String sessionId, String phoneNumber, String currentMenu, String sessionData) {
        Optional<UssdSessionEntity> existingSession = sessionRepository.findBySessionId(sessionId);

        UssdSessionEntity session;
        if (existingSession.isPresent()) {
            session = existingSession.get();
        } else {
            session = new UssdSessionEntity();
            session.setSessionId(sessionId);
            session.setPhoneNumber(phoneNumber);
        }

        session.setCurrentMenu(currentMenu);
        session.setSessionData(sessionData);
        session.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        return sessionRepository.save(session);
    }

    @Override
    public Optional<UssdSessionEntity> getSession(String sessionId) {
        return sessionRepository.findBySessionId(sessionId);
    }

    @Override
    @Transactional
    public void deleteSession(String sessionId) {
        sessionRepository.findBySessionId(sessionId).ifPresent(sessionRepository::delete);
    }

    @Override
    @Transactional
    public void cleanupExpiredSessions() {
        sessionRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}
