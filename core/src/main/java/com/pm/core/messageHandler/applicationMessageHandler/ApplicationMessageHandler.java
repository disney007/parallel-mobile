package com.pm.core.messageHandler.applicationMessageHandler;

import com.pm.core.model.message.ApplicationMessageType;
import com.pm.core.model.message.applicationMessage.ApplicationMessage;

public interface ApplicationMessageHandler {
    ApplicationMessageType getType();

    void handle(ApplicationMessage message, String senderDeviceId);
}
