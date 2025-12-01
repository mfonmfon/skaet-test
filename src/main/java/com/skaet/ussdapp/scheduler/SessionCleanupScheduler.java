package com.skaet.ussdapp.scheduler;

import com.skaet.ussdapp.ussdmodule.service.interfaces.UssdSessionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SessionCleanupScheduler {

    private final UssdSessionService sessionService;

    public SessionCleanupScheduler(UssdSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void cleanupExpiredSessions() {
        sessionService.cleanupExpiredSessions();
    }
}
