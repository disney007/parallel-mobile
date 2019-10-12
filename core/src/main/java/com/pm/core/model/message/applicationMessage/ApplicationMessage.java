package com.pm.core.model.message.applicationMessage;

import com.pm.core.common.Utils;
import com.pm.core.model.message.ApplicationMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationMessage {
    ApplicationMessageType type;
    Object data;

    public <T> T toData(Class<T> clazz) {
        return Utils.convert(data, clazz);
    }
}
