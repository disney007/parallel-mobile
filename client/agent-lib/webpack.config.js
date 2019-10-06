var path = require('path');

module.exports = {
    entry: path.join(__dirname, '/src/Index.ts'),
    output: {
        filename: './dist/agent-lib.js',
        path: __dirname,
        library: "agentLib",   // Important
        libraryTarget: 'umd',   // Important
        umdNamedDefine: true   // Important
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                loader: 'ts-loader',
                exclude: /node_modules/
            },
            {
                test: /\.worker\.js$/,
                use: {loader: 'worker-loader'}
            }
        ]
    },
    resolve: {
        extensions: [".tsx", ".ts", ".js"]
    },
    devtool: 'source-map'
};
