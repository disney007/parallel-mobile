package com.pm.core.service;

import com.pm.core.config.ApplicationConfig;
import com.pm.core.entity.DeviceToken;
import com.pm.core.restModel.ConnectionPermit;
import com.pm.core.model.device.DeviceType;
import com.pm.core.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConnectionService {
    final AgentService agentService;
    final ApplicationConfig applicationConfig;
    final DeviceTokenRepository deviceTokenRepository;

    public ConnectionPermit registerDevice(DeviceType deviceType) {
        String deviceId = agentService.generateDeviceId();
        String token = agentService.generateToken();

        DeviceToken deviceToken = new DeviceToken(deviceId, deviceType, token, System.currentTimeMillis());
        deviceTokenRepository.save(deviceToken);

        return new ConnectionPermit(applicationConfig.getWsUrl(), applicationConfig.getAppId(), deviceId, token, applicationConfig.getMasterUserId());
    }
}
