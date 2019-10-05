var exports = {};
importScripts('./node_modules/requirejs/bin/r.js');
importScripts('./dist/agent-lib.js');


require(["JobRunner"], function (_) {
    new _.JobRunner().run();
});


