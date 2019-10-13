package com.pm.core.model.calculation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalJobRequest {
    String requestId;
    String script;
    String owner;
}
