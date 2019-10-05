import {NetworkChannel} from "./NetworkChannel";

export class JobRunner {
    run() {
        console.log('job runner started');
        const channel = new NetworkChannel();
        channel.connect('wss://m.gl-world.de/ws');
    }
}
