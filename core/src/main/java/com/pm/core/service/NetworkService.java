package com.pm.core.service;

import com.google.common.collect.ImmutableMap;
import com.pm.core.model.MessageType;
import com.pm.core.model.NetworkUser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class NetworkService implements InitializingBean {
    NetworkUser user;


    @Override
    public void afterPropertiesSet() throws Exception {
        user = new NetworkUser();
        user.connect();
    }

    public void authenticate() {
        user.sendMessage(MessageType.AUTH_CLIENT, ImmutableMap.of(
                "appId", "app-id-343",
                "userId", "ANZ-476375",
                "token", "token-12345"
        ));
    }
}
