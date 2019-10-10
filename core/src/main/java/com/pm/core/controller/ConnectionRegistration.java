package com.pm.core.controller;

import com.pm.core.restModel.ConnectionPermit;
import com.pm.core.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/connection")
public class ConnectionRegistration {

    final ConnectionService connectionService;

    @PostMapping(value = "/registerAgent")
    public ConnectionPermit registerAgent() {
        return connectionService.registerAgent();
    }

    @PostMapping(value = "/registerConsumer")
    public ConnectionPermit registerConsumer() {
        return null;
    }
}
