package com.pm.core.service;

import com.google.common.collect.ImmutableMap;
import com.pm.core.common.Utils;
import com.pm.core.config.ApplicationConfig;
import com.pm.core.model.AuthClientReply;
import com.pm.core.model.message.*;
import com.pm.core.model.NetworkChannel;
import com.pm.core.model.message.applicationMessage.ApplicationMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class NetworkService {
    final static int RECONNECT_DELAY = 10;

    NetworkChannel channel;

    final ApplicationConfig applicationConfig;
    final ScheduledExecutorService scheduledExecutorService;
    MessageService messageService;

    public void init(MessageService messageService) {
        this.messageService = messageService;
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

    void fetchMissingMessages() {
        int count = 50;
        log.info("fetch missing message count = {}", count);
        this.sendMessage(MessageType.FETCH_MISSING_MESSAGES_REQUEST, new FetchMissingMessagesRequest(count));
    }

    void handleFetchMissingMessagesComplete(FetchMissingMessagesComplete record) {
        log.info("fetch missing message complete, left count = {}", record.getLeftMissingCount());
        if (record.getLeftMissingCount() > 0) {
            this.fetchMissingMessages();
        }
    }

    void handleAuthReply(AuthClientReply reply) {
        String username = applicationConfig.getMasterUserId();
        if (reply.getIsAuthenticated()) {
            log.info("username [{}] is authenticated", username);
            fetchMissingMessages();
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
            case FETCH_MISSING_MESSAGES_COMPLETE:
                handleFetchMissingMessagesComplete(message.toData(FetchMissingMessagesComplete.class));
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
