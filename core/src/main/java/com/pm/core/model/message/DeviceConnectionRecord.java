package com.pm.core.model.message;

import lombok.Setter;

public class DeviceConnectionRecord {
    @Setter
    String userId;

    public String getDeviceId(){
        return userId;
    }
}
