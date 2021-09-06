const child_process = require("child_process");
const path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyPlugin = require("copy-webpack-plugin");
const { EnvironmentPlugin, ProgressPlugin } = require("webpack");
const pathToPhaser = path.join(__dirname, "/node_modules/phaser/");
const phaser = path.join(pathToPhaser, "dist/phaser.js");
const UglifyJsPlugin = require("uglifyjs-webpack-plugin");

const git = (command) =>
    child_process.execSync(`git ${command}`, { encoding: "utf8" }).trim();

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
        hot: true, // TODO does this help?
    },
    resolve: {
        extensions: [".ts", ".js"],
        alias: {
            phaser,
        },
    },
    optimization: {
        minimizer: [
            new UglifyJsPlugin({
                uglifyOptions: {
                    output: null,
                },
            }),
        ],
    },
    plugins: [
        new ProgressPlugin(),
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
        // Note: setting environment variables in your shell will overwrite these values
        new EnvironmentPlugin({
            GIT_VERSION: git("describe --always"),
            GIT_AUTHOR_DATE: git("log -1 --format=%aI"),
            PRODUCTION: "false",
            NODE_ENV: "development",
        }),
    ],
};
