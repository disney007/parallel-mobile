package com.pm.core.model;

import com.google.common.collect.ImmutableMap;
import com.machinezoo.noexception.Exceptions;
import com.pm.core.common.Utils;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class NetworkUser {


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

    Consumer<NetworkUser> onConnectedCallback;
    Consumer<Throwable> onErrorCallback;
    Consumer<String> onCloseCallback;
    Consumer<String> onMessageCallback;
    Session session;
    String username = "ANZ-123223";


    public void connect() {
        Exceptions.sneak().run(() -> {
            String url = "wss://sandbox-linker.spendzer.app/ws";
            log.info("start connecting to ws [{}]", url);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(new MyClientEndpoint(), URI.create(url));
        });
    }

    void onConnected(Session session) {
        log.info("user [{}] connected to ws", this.username);
        Exceptions.sneak().run(() -> {
            this.session = session;
            if (onConnectedCallback != null) {
                onConnectedCallback.accept(this);
            }
        });

    }


    void onMessage(String message) {
        log.info("user [{}] received message {}", username, message);
        MessageContent testMessage = Utils.fromJson(message, MessageContent.class);
        if (onMessageCallback != null) {
            this.onMessageCallback.accept(message);
        }
    }

    void onClosed(String str) {
        log.info("user [{}] closed", username);
        log.info(str);
        if (this.onMessageCallback != null) {
            this.onCloseCallback.accept(str);
        }
    }

    void onError(Throwable t) {
        log.error("error received from user [{}]", username, t);
        if (onErrorCallback != null) {
            onErrorCallback.accept(t);
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

        Exceptions.sneak().run(() -> this.session.getBasicRemote().sendText(Utils.toJson(message)));
    }
}
