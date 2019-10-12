package com.pm.core.messageHandler;

import com.pm.core.entity.*;
import com.pm.core.model.device.DeviceState;
import com.pm.core.model.device.DeviceType;
import com.pm.core.model.message.DeviceConnectionRecord;
import com.pm.core.model.message.Message;
import com.pm.core.model.message.MessageType;
import com.pm.core.repository.AgentDeviceRepository;
import com.pm.core.repository.ConsumerDeviceRepository;
import com.pm.core.repository.DeviceTokenRepository;
import com.pm.core.repository.DeviceTypeInfoRepository;
import com.pm.core.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceConnectedMessageHandler implements MessageHandler {
    final DeviceTokenRepository deviceTokenRepository;
    final AgentDeviceRepository agentDeviceRepository;
    final ConsumerDeviceRepository consumerDeviceRepository;
    final DeviceTypeInfoRepository deviceTypeInfoRepository;
    final AccountService accountService;

    @Override
    public MessageType getType() {
        return MessageType.USER_CONNECTED;
    }

    @Override
    @Transactional
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

        // random account for demo purpose;
        Account account = accountService.generateRandomAccount();

        DeviceType deviceType = deviceToken.getType();
        DeviceTypeInfo deviceTypeInfo = new DeviceTypeInfo(deviceId, deviceType);
        deviceTypeInfoRepository.save(deviceTypeInfo);

        if (DeviceType.AGENT.equals(deviceType)) {
            agentDeviceRepository.save(new AgentDevice(deviceId, DeviceState.IDLE, account.getUsername(), System.currentTimeMillis()));
        } else if (DeviceType.CONSUMER.equals(deviceType)) {
            consumerDeviceRepository.save(new ConsumerDevice(deviceId, account.getUsername(), System.currentTimeMillis()));
        }

        deviceTokenRepository.delete(deviceToken);
    }
}
