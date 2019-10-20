package com.pm.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeviceDisconnectedEvent {
    String deviceId;
}
