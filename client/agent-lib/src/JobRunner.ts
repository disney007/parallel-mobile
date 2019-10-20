import {NetworkChannel} from "./NetworkChannel";
import {CalculationResponse, MessageContent, MessageType, SubMessage, SubMessageType} from "./Message";

import {Calculator} from "./Calculator";

export class JobRunner {
    channel!: NetworkChannel;
    calculator!: Calculator;

    constructor(private baseUrl: string) {

    }


    run() {
        console.log('starting job runner, baseUrl = ' + this.baseUrl);
        this.calculator = new Calculator();
        this.channel = new NetworkChannel(this.baseUrl);
        this.channel.userMessage.subscribe(msg => this.handleMessage(msg));
        this.channel.connect();
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
