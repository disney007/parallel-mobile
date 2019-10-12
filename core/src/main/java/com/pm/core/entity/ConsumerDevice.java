package com.pm.core.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consumer_device", schema = "public")
public class ConsumerDevice {
    @Id
    @Column(name = "device_id")
    String deviceId;

    @Column(name = "created_timestamp")
    Long createdTimestamp;
}
