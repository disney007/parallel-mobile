package com.pm.core.service;

import com.google.common.collect.ImmutableMap;
import com.pm.core.common.Utils;
import com.pm.core.config.ApplicationConfig;
import com.pm.core.model.AuthClientReply;
import com.pm.core.model.message.ApplicationMessageType;
import com.pm.core.model.message.CustomMessageOut;
import com.pm.core.model.message.Message;
import com.pm.core.model.message.MessageType;
import com.pm.core.model.NetworkChannel;
import com.pm.core.model.message.applicationMessage.ApplicationMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class NetworkService implements InitializingBean {
    final static int RECONNECT_DELAY = 10;

    NetworkChannel channel;

    final ApplicationConfig applicationConfig;
    final ScheduledExecutorService scheduledExecutorService;
    @Setter
    MessageService messageService;

    @Override
    public void afterPropertiesSet() throws Exception {
        setupChannel();
    }

    void setupChannel() {
        log.info("setup channel");
        channel = new NetworkChannel();
        channel.setOnConnected(user -> this.authenticate());
        channel.setOnMessage(this::handleMessage);
        channel.setOnClosed(this::channelClosed);
        channel.connect(applicationConfig.getWsUrl());
    }

    public void authenticate() {
        String username = applicationConfig.getMasterUserId();
        String appId = applicationConfig.getAppId();

        log.info("start authenticating user={}, appId={}", username, appId);
        channel.sendMessage(MessageType.AUTH_CLIENT, ImmutableMap.of(
                "appId", appId,
                "userId", username,
                "token", ""
        ));
    }

    void handleAuthReply(AuthClientReply reply) {
        String username = applicationConfig.getMasterUserId();
        if (reply.getIsAuthenticated()) {
            log.info("username [{}] is authenticated", username);
        } else {
            log.info("username [{}] is not authenticated, close channel", username);
            this.channel.close();
        }
    }

    public void handleMessage(Message message) {
        switch (message.getType()) {
            case AUTH_CLIENT_REPLY:
                handleAuthReply(message.toData(AuthClientReply.class));
                break;
            default:
                assert messageService != null;
                messageService.handleMessage(message);
        }
    }

    public void channelClosed(String reason) {
        log.info("channel closed:[{}], reconnect in [{}] seconds", reason, RECONNECT_DELAY);
        scheduledExecutorService.schedule(this::setupChannel, RECONNECT_DELAY, TimeUnit.SECONDS);
    }

    public void sendMessage(MessageType type, Object payload) {
        channel.sendMessage(type, payload);
    }

    public void sendApplicationMessage(ApplicationMessageType type, Object payload, String to) {
        ApplicationMessage applicationMessage = new ApplicationMessage(type, payload);
        String msgJson = Utils.toJson(applicationMessage);
        sendMessage(MessageType.MESSAGE, new CustomMessageOut(to, msgJson));
    }
}
