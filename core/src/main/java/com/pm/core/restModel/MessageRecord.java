package com.pm.core.restModel;

import com.pm.core.model.message.MessageType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MessageRecord {
    @NotNull
    MessageType type;
    @NotNull
    Object data;
}
