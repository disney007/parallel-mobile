package com.pm.core.service;

import com.pm.core.entity.*;
import com.pm.core.event.AgentDeviceAvailableEvent;
import com.pm.core.event.DeviceConnectedEvent;
import com.pm.core.event.DeviceDisconnectedEvent;
import com.pm.core.model.device.DeviceState;
import com.pm.core.model.device.DeviceType;
import com.pm.core.repository.AgentDeviceRepository;
import com.pm.core.repository.ConsumerDeviceRepository;
import com.pm.core.repository.DeviceTokenRepository;
import com.pm.core.repository.DeviceTypeInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceManagementService {

    final DeviceTokenRepository deviceTokenRepository;
    final AgentDeviceRepository agentDeviceRepository;
    final ConsumerDeviceRepository consumerDeviceRepository;
    final DeviceTypeInfoRepository deviceTypeInfoRepository;
    final AccountService accountService;
    final ApplicationEventPublisher applicationEventPublisher;

    @EventListener(DeviceConnectedEvent.class)
    @Transactional
    public void handleDeviceConnected(DeviceConnectedEvent event) {
        final String deviceId = event.getDeviceId();
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
            applicationEventPublisher.publishEvent(new AgentDeviceAvailableEvent());
        } else if (DeviceType.CONSUMER.equals(deviceType)) {
            consumerDeviceRepository.save(new ConsumerDevice(deviceId, account.getUsername(), System.currentTimeMillis()));
        }

        deviceTokenRepository.delete(deviceToken);
    }

    @EventListener(DeviceDisconnectedEvent.class)
    @Transactional
    public void handleDeviceDisconnected(DeviceDisconnectedEvent event) {
        final String deviceId = event.getDeviceId();
        log.info("device disconnected: [{}]", deviceId);

        deviceTypeInfoRepository.findById(deviceId).ifPresent(deviceTypeInfo -> {
            if (DeviceType.AGENT.equals(deviceTypeInfo.getType())) {
                agentDeviceRepository.deleteById(deviceId);
            } else if (DeviceType.CONSUMER.equals(deviceTypeInfo.getType())) {
                consumerDeviceRepository.deleteById(deviceId);
            }
            deviceTypeInfoRepository.deleteById(deviceId);
        });
    }
}
