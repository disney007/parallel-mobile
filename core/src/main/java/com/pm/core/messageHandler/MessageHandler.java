package com.pm.core.messageHandler;

import com.pm.core.model.message.Message;
import com.pm.core.model.message.MessageType;

public abstract class MessageHandler {
    public abstract MessageType getType();

    public abstract void handle(Message message);
}
