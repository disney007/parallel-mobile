package com.pm.core.model;

import lombok.Data;

@Data
public class AuthClientReply {
    String appId;
    String userId;
    Boolean isAuthenticated;
}
