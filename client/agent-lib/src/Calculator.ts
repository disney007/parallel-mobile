import JobWorker from "worker-loader!./JobWorker";
import {CalculationRequest, CalculationResponse, CalculationState} from "./Message";

export class Calculator {
    jobWorker: JobWorker | undefined;
    resolve: any;

    constructor() {
        this.jobWorker = new JobWorker();
        this.jobWorker.onmessage = this.handleJobWorkMessage.bind(this);
    }

    calculate(request: CalculationRequest): Promise<CalculationResponse> {
        if (!!this.resolve) {
            return Promise.reject({
                state: CalculationState.INTERNAL_ERROR,
                result: "IS_RUNNING",
                id: request.id
            })
        }

        return new Promise((res, rej) => {
            if (!this.jobWorker) {
                rej({
                    state: CalculationState.INTERNAL_ERROR,
                    result: "JOB_WORKER_ERROR",
                    id: request.id
                });
                return;
            }

            this.resolve = res;
            this.jobWorker.postMessage(request);
        });
    }

    handleJobWorkMessage(e: MessageEvent) {
        this.resolve(e.data);
        this.resolve = undefined;
    }

}
