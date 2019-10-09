package com.pm.core.repository;

import com.pm.core.entity.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeviceRepository {

    final MongoTemplate mongoTemplate;

    public void save(Device device) {
        mongoTemplate.save(device);
    }

    public boolean containsDeviceId(String deviceId) {
        return mongoTemplate.findById(deviceId, Device.class) != null;
    }
}
