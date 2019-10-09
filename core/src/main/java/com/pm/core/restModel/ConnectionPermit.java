package com.pm.core.restModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionPermit {
    String wsUrl;
    String appId;
    String deviceId;
    String token;
}
