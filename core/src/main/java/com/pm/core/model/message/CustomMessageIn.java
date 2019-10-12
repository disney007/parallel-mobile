package com.pm.core.model.message;

import com.pm.core.common.Utils;
import com.pm.core.model.message.applicationMessage.ApplicationMessage;
import lombok.Data;

@Data
public class CustomMessageIn {
    String from;
    String content;

    public ApplicationMessage toApplicationMessage() {
        return Utils.fromJson(content, ApplicationMessage.class);
    }
}
