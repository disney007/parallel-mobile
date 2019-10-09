package com.pm.core.model.device;

public enum DeviceState {
    IDLE,
    BUSY, // device is busy with other stuff, can not take jobs
    RUNNING // job is running,
}
