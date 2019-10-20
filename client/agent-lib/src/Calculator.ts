import JobWorker from "worker-loader!./JobWorker";
import {CalculationRequest, CalculationResponse, CalculationState} from "./Message";

export class Calculator {
    jobWorker: JobWorker | undefined;
    resolve: any;

    constructor() {
        this.jobWorker = new JobWorker();
        this.jobWorker.onmessage = this.handleJobWorkMessage.bind(this);
        this.jobWorker.onerror = this.handleError.bind(this);
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
            this.resolve.requestId = request.id;
            this.jobWorker.postMessage(request);
        });
    }

    complete(data: any) {
        this.resolve(data);
        this.resolve = undefined;
    }

    handleJobWorkMessage(e: MessageEvent) {
        this.complete(e.data);
    }

    handleError(e: ErrorEvent) {
        const res: CalculationResponse = {
            id: this.resolve.requestId,
            result: e.message,
            state: CalculationState.SUCCESS
        };
        this.complete(res);
    }

}
