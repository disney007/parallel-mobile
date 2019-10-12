package com.pm.core.messageHandler;

import com.pm.core.model.message.Message;
import com.pm.core.model.message.MessageType;

public interface MessageHandler {
    MessageType getType();

    void handle(Message message);
}
