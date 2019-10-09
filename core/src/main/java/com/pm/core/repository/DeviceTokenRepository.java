package com.pm.core.repository;

import com.pm.core.entity.DeviceToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class DeviceTokenRepository {

    final RedisTemplate<String, Object> redisTemplate;

    public void save(DeviceToken deviceToken) {
        redisTemplate.opsForValue().set(deviceToken.getDeviceId(), deviceToken, 5, TimeUnit.MINUTES);
    }

    public DeviceToken getByDeviceId(String deviceId) {
        return (DeviceToken)redisTemplate.opsForValue().get(deviceId);
    }

    public void delete(DeviceToken deviceToken) {
        redisTemplate.delete(deviceToken.getDeviceId());
    }
}
