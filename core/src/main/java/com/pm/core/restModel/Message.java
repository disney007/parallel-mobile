package com.pm.core.restModel;

import com.pm.core.model.MessageType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Message {
    @NotNull
    MessageType type;
    @NotNull
    Object data;
}
