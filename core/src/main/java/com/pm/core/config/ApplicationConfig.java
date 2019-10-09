package com.pm.core.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@Data
@Slf4j
public class ApplicationConfig {
    @Value("${minDeviceId}")
    Long minDeviceId;
    @Value("${maxDeviceId}")
    Long maxDeviceId;
    @Value("${appId}")
    String appId;
    @Value("${wsUrl}")
    String wsUrl;
    @Value("${masterUserId}")
    String masterUserId;
    @Value("${deviceIdPrefix}")
    String deviceIdPrefix;
}
