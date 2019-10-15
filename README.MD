This is project is to run small tasks on mobile and collect results. The messaging system is using https://github.com/disney007/WebSocketaaS

### Example:
Request:
```json
{
   "type":"MESSAGE",
   "data":{
      "to":"ANZ-123223",
      "content":"{\"type\":\"CAL_REQ\",\"data\":{\"script\":\"var a=[1+1];a;\",\"id\": \"xxx\"}}"
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
