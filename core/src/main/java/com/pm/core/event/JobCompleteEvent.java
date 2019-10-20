package com.pm.core.event;

import com.pm.core.model.calculation.CalJobResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobCompleteEvent {
    CalJobResult result;
}
