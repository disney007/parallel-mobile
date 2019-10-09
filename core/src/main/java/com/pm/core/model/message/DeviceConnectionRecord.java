package com.pm.core.model.message;

import lombok.Data;

@Data
public class DeviceConnectionRecord {
    String userId;

    public String getDeviceId(){
        return userId;
    }
}
