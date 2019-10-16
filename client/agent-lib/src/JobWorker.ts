import {CalculationRequest, CalculationResponse, CalculationState} from "./Message";

const ctx: Worker = self as any;

ctx.addEventListener("message", (event) => {
    console.log('job worker: job received');
    doCalculation(event.data).then(res => {
        console.log('job worker: job finish');
        ctx.postMessage(res);
    });
});

function evalScript(script: string): Promise<any> {
    function evalFull() {
        try {
            const begin = "(function fun(){";
            const end = "})()";
            return eval(begin + script + end)
        } catch (e) {
            return e.message;
        }
    }

    return Promise.resolve(evalFull());
}

function doCalculation(req: CalculationRequest): Promise<CalculationResponse> {
    if (typeof WorkerGlobalScope !== 'undefined' && self instanceof WorkerGlobalScope) {
        console.log('running in web worker')
    }
    const res: CalculationResponse = {
        id: req.id,
        result: "",
        state: CalculationState.SUCCESS
    };

    console.log('calculation job received, id = ', req.id);
    return evalScript(req.script)
        .then(data => {
            res.result = JSON.stringify(data);
            console.log('calculation job finished, id = ', req.id);
            return res;
        })
        .catch(e => {
            res.result = JSON.stringify(e);
            console.log('calculation job finished with error, id = ', req.id);
            return res;
        });
}


console.log('worker started');
