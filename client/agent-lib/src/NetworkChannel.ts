export class NetworkChannel {

    socket: WebSocket | undefined;

    connect(url: string) {
        this.socket = new WebSocket(url);
        this.socket.onopen = this.onOpen.bind(this);
        this.socket.onmessage = this.onMessage.bind(this);
        this.socket.onclose = this.onClosed.bind(this);
        this.socket.onerror = this.onError.bind(this);
    }

    onMessage(event: MessageEvent) {
        console.log(event.data);
    }

    onOpen() {
        console.log('websocket is open');
    }

    onClosed(ev: CloseEvent) {
        console.log('websocket is closed', ev.reason);
    }

    onError(ev: any) {
        console.log('error:', ev.message)
    }

    public sendMessage(obj: any) {
        if (this.socket) {
            this.socket.send(JSON.stringify(obj));
        } else {
            console.log('socket is not open')
        }
    }
}
