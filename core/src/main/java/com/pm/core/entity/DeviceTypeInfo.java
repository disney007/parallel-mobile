package com.pm.core.entity;

import com.pm.core.model.device.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device_type", schema = "public")
public class DeviceTypeInfo {
    @Id
    @Column(name = "device_id")
    String deviceId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    DeviceType type;
}
