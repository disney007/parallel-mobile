package com.pm.core.service;

import com.pm.core.config.ApplicationConfig;
import com.pm.core.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgentService {
    final static String DEVICE_ID_KEY = "DEVICE_ID_KEY";
    final RedisTemplate redisTemplate;
    final ApplicationConfig applicationConfig;
    final AgentRepository agentRepository;

    public Long getNextDeviceId() {
        long min = applicationConfig.getMinDeviceId();
        long max = applicationConfig.getMaxDeviceId();
        return redisTemplate.opsForValue().increment(DEVICE_ID_KEY) % (max - min) + 1;
    }

    public String generateDeviceId() {
        //todo: make device id generator stable
        return makeDeviceId(getNextDeviceId());
    }

    public String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }

    String makeDeviceId(Long number) {
        return applicationConfig.getDeviceIdPrefix() + "-" + number;
    }
}
