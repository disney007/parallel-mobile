package com.pm.core.model;

import com.google.common.collect.ImmutableMap;
import com.machinezoo.noexception.Exceptions;
import com.pm.core.common.Utils;
import com.pm.core.model.message.Message;
import com.pm.core.model.message.MessageType;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import java.net.URI;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class NetworkChannel {


    @ClientEndpoint
    public class MyClientEndpoint {
        public MyClientEndpoint() {
        }

        @OnOpen
        public void onOpen(Session session) {
            onConnected(session);
        }

        @OnMessage
        public void processMessage(String message) {
            onMessage(message);
        }

        @OnError
        public void processError(Throwable t) {
            onError(t);
        }

        @OnClose
        public void close(CloseReason closeReason) {
            onClosed(closeReason.toString());
        }
    }

    @Setter
    Consumer<NetworkChannel> onConnected;
    @Setter
    Consumer<Throwable> onError;
    @Setter
    Consumer<String> onClosed;
    @Setter
    Consumer<Message> onMessage;
    Session session;


    public void connect(String url) {
        Exceptions.sneak().run(() -> {
            log.info("start connecting to ws [{}]", url);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(new MyClientEndpoint(), URI.create(url));
        });
    }

    void onConnected(Session session) {
        log.info("connected to ws");
        Exceptions.sneak().run(() -> {
            this.session = session;
            if (onConnected != null) {
                onConnected.accept(this);
            }
        });
    }


    void onMessage(String message) {
        log.debug("on message received [{}]", message);
        Message testMessage = Utils.fromJson(message, Message.class);
        if (onMessage != null) {
            this.onMessage.accept(testMessage);
        }
    }

    void onClosed(String str) {
        log.info("connection closed:{}", str);
        if (this.onMessage != null) {
            this.onClosed.accept(str);
        }
    }

    void onError(Throwable t) {
        log.error("error received", t);
        if (onError != null) {
            onError.accept(t);
        }
    }

    boolean isConnected() {
        return this.session.isOpen();
    }

    public void sendMessage(MessageType type, Object payload) {
        if (!isConnected()) {
            log.warn("network user is not connected");
            return;
        }
        Map<String, Object> message = ImmutableMap.of(
                "type", type,
                "data", payload,
                "feature", "RELIABLE"
        );

        String msgJson = Utils.toJson(message);
        log.debug("send message:[{}]", msgJson);
        Exceptions.sneak().run(() -> this.session.getBasicRemote().sendText(msgJson));
    }

    public void close() {
        log.info("close channel");
        Exceptions.sneak().run(() -> this.session.close());
    }
}
