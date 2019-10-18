This is project is to run small tasks on mobile and collect results. The messaging system is using https://github.com/disney007/WebSocketaaS

### Example:
Request:
```json
{
   "type":"MESSAGE",
   "data":{
      "to":"ANZ-123223",
      "content":"{\"type\":\"CAL_REQ\",\"data\":{\"script\":\"var a=[1+1];return a;\",\"id\": \"xxx\"}}"
   }
}
```
Response:
```json
{
   "type":"MESSAGE",
   "data":{
      "from":"ANZ-123223",
      "content":"{\"type\":\"CAL_RES\",\"data\":{\"id\":\"xxx\",\"result\":[2],\"state\":\"OK\"}}"
   }
}
```
### Live Demo
- Open https://pm.gl-world.de/agent-index.html and serve as agent device to provide computation power
- Open https://pm.gl-world.de/consumer-index.html to simulate computation consumer
- You can write javascript code in left panel of consumer page.
- Click `Calculate` button and the script will be sent to agent device to compute.
- The computation result will be sent back and display on the right panel of consumer page.

Note:
- The result value is given by `return` keyword in javascript.
- You can open console log of agent device page to view the computation process, where the script gets executed
in webworker
 

