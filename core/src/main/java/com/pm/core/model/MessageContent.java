package com.pm.core.model;

import com.pm.core.common.Utils;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MessageContent {
    MessageType type;
    Object data;
    String reference;

    public <T> T toData(Class<T> clazz) {
        return Utils.convert(data, clazz);
    }
}
