package com.pm.core.service;

import com.pm.core.config.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilService {
    final static String DEVICE_ID_KEY = "DEVICE_ID_KEY";
    final RedisTemplate redisTemplate;
    final ApplicationConfig applicationConfig;

    public Long getNextDeviceId() {
        long min = applicationConfig.getMinDeviceId();
        long max = applicationConfig.getMaxDeviceId();
        return redisTemplate.opsForValue().increment(DEVICE_ID_KEY) % (max - min) + 1;
    }
}
