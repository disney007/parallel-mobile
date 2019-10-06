import {NetworkChannel} from "./NetworkChannel";
import {
    CalculationRequest,
    CalculationResponse,
    CalculationState, Message, MessageContent,
    MessageType,
    SubMessage,
    SubMessageType
} from "./Message";

export class JobRunner {
    channel: NetworkChannel | undefined;

    run() {
        console.log('job runner started');
        this.channel = new NetworkChannel('wss://m.gl-world.de/ws', 'app-id-343', 'ANZ-990289');
        this.channel.userMessage.subscribe(msg => this.handleMessage(msg));
        this.channel.connect();
    }

    handleMessage(msg: MessageContent) {
        const subMessage = <SubMessage>JSON.parse(msg.content);
        switch (subMessage.type) {
            case SubMessageType.CAL_REQ:
                const res = this.doCalculation(subMessage.content);
                if (this.channel) {
                    this.channel.sendMessage(MessageType.MESSAGE, {
                        to: msg.from,
                        content:JSON.stringify({
                            type: SubMessageType.CAL_RES,
                            data: res
                        })
                    })
                }

                break;
            default:
                console.log('unhandled submessage', subMessage);
        }
    }

    doCalculation(req: CalculationRequest): CalculationResponse {
        const res: CalculationResponse = {
            id: req.id,
            result: "",
            state: CalculationState.OK
        };
        try {
            res.result = eval(req.script);
            console.log("calculation for ", req.id, " has complete, the result is", res.result);
        } catch (e) {
            res.result = e.message;
            res.state = CalculationState.ERROR;
        }
        return res;

    }
}
