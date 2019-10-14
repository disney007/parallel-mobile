import {CalculationRequest, CalculationResponse, CalculationState} from "./Message";

const ctx: Worker = self as any;

ctx.addEventListener("message", (event) => {
    console.log('job worker: job received');
    const res = doCalculation(event.data);
    console.log('job worker: job finish');
    ctx.postMessage(res);
});

function doCalculation(req: CalculationRequest): CalculationResponse {
    if (typeof WorkerGlobalScope !== 'undefined' && self instanceof WorkerGlobalScope) {
        console.log('running in web worker')
    }
    const res: CalculationResponse = {
        id: req.id,
        result: "",
        state: CalculationState.SUCCESS
    };
    try {
        console.log('calculation job received, id = ', req.id);
        res.result = JSON.stringify(eval(req.script));
        console.log('calculation job finished, id = ', req.id);
    } catch (e) {
        res.result = e.message;
        res.state = CalculationState.ERROR;
    }
    return res;
}


console.log('worker started');
