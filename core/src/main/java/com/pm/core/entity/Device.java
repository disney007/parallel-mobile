package com.pm.core.entity;

import com.pm.core.model.device.DeviceState;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "device")
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    @Id
    String id;
    @Indexed
    DeviceState state;
    Long createdDateTime;
}
