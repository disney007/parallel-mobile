package com.pm.core.event;

import com.pm.core.model.calculation.CalJobRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobRequestEvent {
    CalJobRequest jobRequest;
}
