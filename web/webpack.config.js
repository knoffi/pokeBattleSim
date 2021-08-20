const path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyPlugin = require("copy-webpack-plugin");
const pathToPhaser = path.join(__dirname, "/node_modules/phaser/");
const phaser = path.join(pathToPhaser, "dist/phaser.js");

module.exports = {
    entry: "./src/index.ts",
    output: {
        path: path.resolve(__dirname, "build"),
        filename: "bundle.js",
    },
    module: {
        rules: [
            { test: /\.ts$/, loader: "ts-loader", exclude: "/node_modules/" },
            { test: /phaser\.js$/, loader: "expose-loader?Phaser" },
        ],
    },
    devServer: {
        contentBase: path.resolve(__dirname, "./"),
        publicPath: "/build/",
        host: "localhost",
        port: 8080,
        open: true,
    },
    resolve: {
        extensions: [".ts", ".js"],
        alias: {
            phaser,
        },
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: "./index.template.html",
            filename: "index.html",
            inject: "body",
        }),
        new CopyPlugin({
            patterns: [
                { from: "assets", to: "assets" },
                { from: "index.css", to: "index.css" },
            ],
        }),
    ],
};
