import {NetworkChannel} from "./NetworkChannel";

export class JobRunner {
    run() {
        console.log('job runner started');
        const channel = new NetworkChannel('wss://m.gl-world.de/ws', 'app-id-343', 'ANZ-990289');
        channel.userMessage.subscribe(msg => console.log(msg));
        channel.connect();
    }
}
