export enum MessageType {
    MESSAGE = 'MESSAGE',
    GENERAL_ERROR = 'GENERAL_ERROR',
    AUTH_REQUIRED = 'AUTH_REQUIRED',
    USER_CONNECTED = 'USER_CONNECTED',
    USER_DISCONNECTED = 'USER_DISCONNECTED',
    AUTH_CLIENT = 'AUTH_CLIENT',
    AUTH_CLIENT_REPLY = 'AUTH_CLIENT_REPLY',
}

export interface Message {
    type: MessageType;
    data: any
}

export interface AuthClientReply {
    appId: string;
    userId: string;
    isAuthenticated: boolean;
}
