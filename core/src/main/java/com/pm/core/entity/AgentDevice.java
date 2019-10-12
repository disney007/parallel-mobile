package com.pm.core.entity;

import com.pm.core.model.device.DeviceState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "agent_device", schema = "public")
public class AgentDevice {
    @Id
    @Column(name = "device_id")
    String deviceId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    DeviceState state;

    @Column(name = "owner")
    String owner;

    @Column(name = "created_timestamp")
    Long createdTimestamp;
}
