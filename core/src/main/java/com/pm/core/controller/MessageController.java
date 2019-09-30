package com.pm.core.controller;

import com.pm.core.service.NetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    final NetworkService networkService;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public void auth() {
        networkService.authenticate();
    }
}
