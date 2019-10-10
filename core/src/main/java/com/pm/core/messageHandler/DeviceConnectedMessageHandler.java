package com.pm.core.messageHandler;

import com.pm.core.entity.Agent;
import com.pm.core.entity.DeviceToken;
import com.pm.core.model.device.DeviceState;
import com.pm.core.model.device.DeviceType;
import com.pm.core.model.message.DeviceConnectionRecord;
import com.pm.core.model.message.Message;
import com.pm.core.model.message.MessageType;
import com.pm.core.repository.AgentRepository;
import com.pm.core.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceConnectedMessageHandler extends MessageHandler {
    final DeviceTokenRepository deviceTokenRepository;
    final AgentRepository agentRepository;

    @Override
    public MessageType getType() {
        return MessageType.USER_CONNECTED;
    }

    @Override
    public void handle(Message message) {
        final DeviceConnectionRecord record = message.toData(DeviceConnectionRecord.class);
        final String deviceId = record.getDeviceId();
        log.info("device connected: [{}]", deviceId);

        DeviceToken deviceToken = deviceTokenRepository.getByDeviceId(deviceId);
        if (deviceToken == null) {
            // todo: disconnect current device
            log.info("token not found for device id [{}], disconnect", deviceId);
            return;
        }
        if (DeviceType.AGENT.equals(deviceToken.getType())) {
            Agent agent = new Agent(deviceId, DeviceState.IDLE, System.currentTimeMillis());
            agentRepository.save(agent);
            deviceTokenRepository.delete(deviceToken);
        }
    }
}
