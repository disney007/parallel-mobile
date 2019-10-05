package com.pm.core.service;

import com.google.common.collect.ImmutableMap;
import com.pm.core.model.AuthClientReply;
import com.pm.core.model.MessageContent;
import com.pm.core.model.MessageType;
import com.pm.core.model.NetworkChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class NetworkService implements InitializingBean {
    final String username = "ANZ-123223";
    final String appId = "app-id-343";
    final static int RECONNECT_DELAY = 10;

    NetworkChannel channel;

    final ScheduledExecutorService scheduledExecutorService;

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
        channel.connect("wss://m.gl-world.de/ws");
    }

    public void authenticate() {
        log.info("start authenticating user={}, appId={}", this.username, this.appId);
        channel.sendMessage(MessageType.AUTH_CLIENT, ImmutableMap.of(
                "appId", this.appId,
                "userId", this.username,
                "token", ""
        ));
    }

    void handleAuthReply(AuthClientReply reply) {
        if (reply.getIsAuthenticated()) {
            log.info("username [{}] is authenticated", this.username);
        } else {
            log.info("username [{}] is not authenticated, close channel", this.username);
            this.channel.close();
        }
    }

    public void handleMessage(MessageContent message) {
        switch (message.getType()) {
            case AUTH_CLIENT_REPLY:
                handleAuthReply(message.toData(AuthClientReply.class));
                break;
            default:
                log.info("unhandled received message:[{}]", message);
        }
    }

    public void channelClosed(String reason) {
        log.info("channel closed:[{}], reconnect in [{}] seconds", reason, RECONNECT_DELAY);
        scheduledExecutorService.schedule(this::setupChannel, RECONNECT_DELAY, TimeUnit.SECONDS);
    }

    public void sendMessage(MessageType type, Object payload) {
        channel.sendMessage(type, payload);
    }
}
