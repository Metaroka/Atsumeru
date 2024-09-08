package com.atsumeru.web.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class BonjourService {

    public BonjourService() {
    }

    public void restartService() {
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(9)
    public void startService() {
    }

    public void stopService() {
    }

    public boolean isServiceStarted() {
        return false;
    }
}
