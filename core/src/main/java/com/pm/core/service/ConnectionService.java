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
    final DeviceService deviceService;
    final ApplicationConfig applicationConfig;
    final DeviceTokenRepository deviceTokenRepository;

    public ConnectionPermit registerAgent() {
        String deviceId = deviceService.generateDeviceId();
        String token = deviceService.generateToken();

        DeviceToken deviceToken = new DeviceToken(deviceId, DeviceType.AGENT, token, System.currentTimeMillis());
        deviceTokenRepository.save(deviceToken);

        return new ConnectionPermit(applicationConfig.getWsUrl(), applicationConfig.getAppId(), deviceId, token);
    }
}
