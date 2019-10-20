package com.pm.core.service;

import com.pm.core.common.Utils;
import com.pm.core.messageHandler.MessageHandler;
import com.pm.core.model.message.Message;
import com.pm.core.model.message.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
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

    public void setup() {
        messageHandlers = applicationContext.getBeansOfType(MessageHandler.class)
                .values().stream().collect(Collectors.toMap(MessageHandler::getType, m -> m));
        assert messageHandlers.size() == 3;
        networkService.init(this);
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        this.setup();
    }

    public void handleMessage(Message message) {
        MessageType type = message.getType();
        if (!messageHandlers.containsKey(type)) {
            log.warn("message handler not found for message [{}]", Utils.toJson(message));
            return;
        }

        try {
            messageHandlers.get(type).handle(message);
        } catch (Exception e) {
            log.error("error occurred in message handler", e);
        }


    }
}
