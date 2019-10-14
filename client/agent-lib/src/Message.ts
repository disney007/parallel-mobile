export enum MessageType {
    MESSAGE = 'MESSAGE',
    GENERAL_ERROR = 'GENERAL_ERROR',
    AUTH_REQUIRED = 'AUTH_REQUIRED',
    USER_CONNECTED = 'USER_CONNECTED',
    USER_DISCONNECTED = 'USER_DISCONNECTED',
    AUTH_CLIENT = 'AUTH_CLIENT',
    AUTH_CLIENT_REPLY = 'AUTH_CLIENT_REPLY',
}

export enum SubMessageType {
    CAL_REQ = 'CAL_REQ',
    CAL_RES = 'CAL_RES'
}

export interface Message {
    type: MessageType;
    data: any;
}

export interface MessageContent {
    from: string;
    content: any
}

export interface SubMessage {
    type: SubMessageType;
    data: any;
}

export interface AuthClientReply {
    appId: string;
    userId: string;
    isAuthenticated: boolean;
}

export interface CalculationRequest {
    id: string;
    script: string;
}

export enum CalculationState {
    SUCCESS = 'SUCCESS',
    ERROR = 'ERROR',
    INTERNAL_ERROR = 'INTERNAL_ERROR'
}

export interface CalculationResponse {
    state: CalculationState
    result: string,
    id: string
}

export interface ConnectionPermit {
    wsUrl: string,
    appId: string;
    deviceId: string;
    token: string;
}
