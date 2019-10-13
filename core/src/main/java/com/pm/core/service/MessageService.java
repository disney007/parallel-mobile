package com.pm.core.service;

import com.google.common.collect.ImmutableMap;
import com.pm.core.common.Utils;
import com.pm.core.messageHandler.CustomMessageHandler;
import com.pm.core.messageHandler.DeviceConnectedMessageHandler;
import com.pm.core.messageHandler.DeviceDisconnectedMessageHandler;
import com.pm.core.messageHandler.MessageHandler;
import com.pm.core.model.message.Message;
import com.pm.core.model.message.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    final ApplicationContext applicationContext;
    final NetworkService networkService;
    Map<MessageType, MessageHandler> messageHandlers;

    @PostConstruct
    public void setup() {
        networkService.setMessageService(this);
        messageHandlers = applicationContext.getBeansOfType(MessageHandler.class)
                .values().stream().collect(Collectors.toMap(MessageHandler::getType, m -> m));
        assert messageHandlers.size() == 3;
    }

    public void handleMessage(Message message) {
        MessageType type = message.getType();
        if (!messageHandlers.containsKey(type)) {
            log.warn("messageRecord handler not found for messageRecord [{}]", Utils.toJson(message));
            return;
        }

        try {
            messageHandlers.get(type).handle(message);
        } catch (Exception e) {
            log.error("error occurred in message handler", e);
        }


    }
}
