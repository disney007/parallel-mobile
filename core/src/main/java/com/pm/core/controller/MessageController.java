package com.pm.core.controller;

import com.pm.core.restModel.Message;
import com.pm.core.service.NetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    final NetworkService networkService;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public void auth() {
        networkService.authenticate();
    }

    @RequestMapping(value = "send", method = RequestMethod.POST)
    public void sendMessage(@Valid @RequestBody Message message) {
        networkService.sendMessage(message.getType(), message.getData());
    }
}
