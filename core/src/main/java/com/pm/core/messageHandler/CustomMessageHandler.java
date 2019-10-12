package com.pm.core.messageHandler;

import com.pm.core.messageHandler.applicationMessageHandler.ApplicationMessageHandler;
import com.pm.core.model.message.ApplicationMessageType;
import com.pm.core.model.message.CustomMessageIn;
import com.pm.core.model.message.Message;
import com.pm.core.model.message.MessageType;
import com.pm.core.model.message.applicationMessage.ApplicationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomMessageHandler implements MessageHandler {
    final ApplicationContext applicationContext;

    Map<ApplicationMessageType, ApplicationMessageHandler> messageHandlers;

    @PostConstruct
    public void setup() {
        messageHandlers = applicationContext.getBeansOfType(ApplicationMessageHandler.class)
                .values().stream().collect(Collectors.toMap(ApplicationMessageHandler::getType, m -> m));
    }

    @Override
    public MessageType getType() {
        return MessageType.MESSAGE;
    }

    @Override
    public void handle(Message message) {
        CustomMessageIn customMessage = message.toData(CustomMessageIn.class);
        ApplicationMessage applicationMessage = customMessage.toApplicationMessage();
        if (!messageHandlers.containsKey(applicationMessage.getType())) {
            log.warn("application message handler not found for [{}]", applicationMessage.getType());
            return;
        }

        messageHandlers.get(applicationMessage.getType()).handle(applicationMessage, customMessage.getFrom());
    }
}
