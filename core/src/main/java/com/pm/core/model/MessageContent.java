package com.pm.core.model;

import lombok.Data;

@Data
public class MessageContent {
    MessageType type;
    Object data;
    String reference;
}
