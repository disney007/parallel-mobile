package com.pm.core.entity;

import com.pm.core.model.device.DeviceState;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;


@NoArgsConstructor
@Entity
@Table(name = "agent", schema = "public")
public class Agent {
    @Id
    @GeneratedValue
    UUID id;

    @Column(name = "device_id")
    String deviceId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    DeviceState state;

    @Column(name = "created_timestamp")
    Long createdTimestamp;

    public Agent(String deviceId, DeviceState state, Long createdTimestamp) {
        this.deviceId = deviceId;
        this.state = state;
        this.createdTimestamp = createdTimestamp;
    }
}
