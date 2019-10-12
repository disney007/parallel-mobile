package com.pm.core.messageHandler;

import com.pm.core.model.device.DeviceType;
import com.pm.core.model.message.DeviceConnectionRecord;
import com.pm.core.model.message.Message;
import com.pm.core.model.message.MessageType;
import com.pm.core.repository.AgentDeviceRepository;
import com.pm.core.repository.ConsumerDeviceRepository;
import com.pm.core.repository.DeviceTypeInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceDisconnectedMessageHandler implements MessageHandler {

    final AgentDeviceRepository agentDeviceRepository;
    final ConsumerDeviceRepository consumerDeviceRepository;
    final DeviceTypeInfoRepository deviceTypeInfoRepository;

    @Override
    public MessageType getType() {
        return MessageType.USER_DISCONNECTED;
    }

    @Override
    @Transactional
    public void handle(Message message) {
        final DeviceConnectionRecord record = message.toData(DeviceConnectionRecord.class);
        final String deviceId = record.getDeviceId();
        log.info("device disconnected: [{}]", deviceId);

        deviceTypeInfoRepository.findById(deviceId)
                .ifPresent(deviceTypeInfo -> {
                    if (DeviceType.AGENT.equals(deviceTypeInfo.getType())) {
                        agentDeviceRepository.deleteById(deviceId);
                    } else if (DeviceType.CONSUMER.equals(deviceTypeInfo.getType())) {
                        consumerDeviceRepository.deleteById(deviceId);
                    }
                    deviceTypeInfoRepository.deleteById(deviceId);
                });
    }
}
