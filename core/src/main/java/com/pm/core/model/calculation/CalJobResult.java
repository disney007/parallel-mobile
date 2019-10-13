package com.pm.core.model.calculation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalJobResult {
    UUID id;
    String result;
    CalJobExecState state;
}
