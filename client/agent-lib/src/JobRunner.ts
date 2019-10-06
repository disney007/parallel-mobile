import {NetworkChannel} from "./NetworkChannel";
import {CalculationResponse, MessageContent, MessageType, SubMessage, SubMessageType} from "./Message";

import {Calculator} from "./Calculator";

export class JobRunner {
    channel!: NetworkChannel;
    calcualtor!: Calculator;

    run() {
        console.log('job runner started');
        this.calcualtor = new Calculator();
        this.channel = new NetworkChannel('wss://m.gl-world.de/ws', 'app-id-343', 'ANZ-990289');
        this.channel.userMessage.subscribe(msg => this.handleMessage(msg));
        this.channel.connect();
    }

    handleMessage(msg: MessageContent) {
        const subMessage = <SubMessage>JSON.parse(msg.content);
        switch (subMessage.type) {
            case SubMessageType.CAL_REQ:
                this.calcualtor.calculate(subMessage.content).then(res => this.handleCalculationResponse(res, msg.from));
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
