import {NetworkChannel} from "./NetworkChannel";
import {
    CalculationResponse,
    ConnectionPermit,
    MessageContent,
    MessageType,
    SubMessage,
    SubMessageType
} from "./Message";
import axios from 'axios';

import {Calculator} from "./Calculator";

export class JobRunner {
    channel!: NetworkChannel;
    calculator!: Calculator;

    constructor(private baseUrl: string) {

    }

    registerAgent(): Promise<ConnectionPermit> {
        return axios.post<ConnectionPermit>(this.baseUrl + '/api/connection/registerAgent')
            .then(response => response.data)
    }

    run() {
        console.log('starting job runner, baseUrl = ' + this.baseUrl);
        this.calculator = new Calculator();
        this.registerAgent()
            .then(connection => {
                console.log('got connection permit, device id = ', connection.deviceId);
                this.channel = new NetworkChannel(connection.wsUrl, connection.appId, connection.deviceId, connection.token);
                this.channel.userMessage.subscribe(msg => this.handleMessage(msg));
                this.channel.connect();
            })

    }

    handleMessage(msg: MessageContent) {
        const subMessage = <SubMessage>JSON.parse(msg.content);
        switch (subMessage.type) {
            case SubMessageType.CAL_REQ:
                this.calculator.calculate(subMessage.data).then(res => this.handleCalculationResponse(res, msg.from));
                break;
            default:
                console.log('unhandled submessage', subMessage);
        }
    }

    handleCalculationResponse(res: CalculationResponse, fromUserId: string) {
        this.channel.sendMessage(MessageType.MESSAGE, {
            to: fromUserId,
            content: JSON.stringify({
                type: SubMessageType.CAL_RES,
                data: res
            })
        })
    }
}
