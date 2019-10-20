package com.pm.core.messageHandler;

import com.pm.core.event.DeviceConnectedEvent;
import com.pm.core.model.message.DeviceConnectionRecord;
import com.pm.core.model.message.Message;
import com.pm.core.model.message.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceConnectedMessageHandler implements MessageHandler {
    final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public MessageType getType() {
        return MessageType.USER_CONNECTED;
    }

    @Override
    public void handle(Message message) {
        final DeviceConnectionRecord record = message.toData(DeviceConnectionRecord.class);
        applicationEventPublisher.publishEvent(new DeviceConnectedEvent(record.getDeviceId()));
    }
}
