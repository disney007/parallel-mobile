package com.pm.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ResultSentToConsumerEvent {
    UUID jobId;
}
