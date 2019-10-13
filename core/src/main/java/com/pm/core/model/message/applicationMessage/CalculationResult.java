package com.pm.core.model.message.applicationMessage;

import com.pm.core.model.calculation.CalJobExecState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculationResult {
    String id;
    String result;
    CalJobExecState state;
}
