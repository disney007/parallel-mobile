package com.pm.core.entity;

import com.pm.core.model.device.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceToken implements Serializable {

    private static final long serialVersionUID = 8466154171171893154L;
    String deviceId;
    DeviceType type;
    String token;
    Long createdTime;
}
