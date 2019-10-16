import {AuthClientReply, ConnectionPermit, Message, MessageContent, MessageType, SubMessage} from "./Message";
import {Subject} from "rxjs";
import axios from "axios";

export class NetworkChannel {

    socket: WebSocket | undefined;
    userMessage: Subject<MessageContent> = new Subject<MessageContent>();
    connectionPermit!: ConnectionPermit;

    constructor(private baseUrl: string) {
    }

    registerAgent(): Promise<ConnectionPermit> {
        return axios.post<ConnectionPermit>(this.baseUrl + '/api/connection/registerAgent')
            .then(response => response.data)
    }

    connect() {
        this.registerAgent()
            .then(connectionPermit => {
                this.connectionPermit = connectionPermit;

                console.log('got connection permit, device id = ', connectionPermit.deviceId);
                console.log('start connecting to web socket', this.connectionPermit.wsUrl);
                this.socket = new WebSocket(this.connectionPermit.wsUrl);
                this.socket.onopen = this.onOpen.bind(this);
                this.socket.onmessage = this.onMessage.bind(this);
                this.socket.onclose = this.onClosed.bind(this);
                this.socket.onerror = this.onError.bind(this);
            })
            .catch((e) => {
                console.log('error occurred during agent registration', e);
            });
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
            "appId": this.connectionPermit.appId,
            "userId": this.connectionPermit.deviceId,
            "token": this.connectionPermit.token
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

    handleUserMessage(messageContent: MessageContent) {
        this.userMessage.next(messageContent);
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
