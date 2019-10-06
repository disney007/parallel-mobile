import {AuthClientReply, Message, MessageType} from "./Message";
import {Subject} from "rxjs";

export class NetworkChannel {

    socket: WebSocket | undefined;
    url: string;
    appId: string;
    userId: string;
    userMessage: Subject<any> = new Subject<any>();

    constructor(url: string, appId: string, userId: string) {
        this.url = url;
        this.appId = appId;
        this.userId = userId;
    }

    connect() {
        console.log('start connecting to web socket', this.url);
        this.socket = new WebSocket(this.url);
        this.socket.onopen = this.onOpen.bind(this);
        this.socket.onmessage = this.onMessage.bind(this);
        this.socket.onclose = this.onClosed.bind(this);
        this.socket.onerror = this.onError.bind(this);
    }

    reconnect() {
        console.log('reconnect in 10 seconds');
        setTimeout(() => this.connect(), 10000);
    }

    onMessage(event: MessageEvent) {
        const message = <Message>JSON.parse(event.data);
        switch (message.type) {
            case MessageType.AUTH_CLIENT_REPLY:
                this.handleAuthReply(message.data);
                break;
            case MessageType.MESSAGE:
                this.handleUserMessage(message.data);
                break;
            default:
                console.log('unhandled recieved message', message);

        }
    }

    onOpen() {
        console.log('websocket is open');
        this.authenticateClient();
    }

    onClosed(ev: CloseEvent) {
        console.log('websocket is closed', ev.reason);
        this.reconnect();
    }

    onError(ev: any) {
        console.log('error:', ev.message);
        this.reconnect();
    }

    authenticateClient() {
        console.log('authenticate client');
        this.sendMessage(MessageType.AUTH_CLIENT, {
            "appId": this.appId,
            "userId": this.userId,
            "token": ""
        })
    }

    handleAuthReply(reply: AuthClientReply) {
        if (reply.isAuthenticated) {
            console.log('user is authenticated')
        } else {
            console.log('authentication failed, closing');
            this.close();
        }
    }

    handleUserMessage(userMessage: any) {
        this.userMessage.next(userMessage);
    }


    public sendMessage(type: MessageType, data: any) {
        if (this.socket) {
            const message = {type, data};
            this.socket.send(JSON.stringify(message));
        } else {
            console.log('socket is not open')
        }
    }

    public close() {
        if (this.socket) {
            this.socket.close();
        }
    }
}
