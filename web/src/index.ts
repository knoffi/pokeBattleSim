import { Game } from "phaser";
import { gameConfig } from "./game-config";

main();

function main() {
    window.addEventListener("load", () => {
        console.log("version:", process.env.GIT_VERSION);
        console.log("git commit date:", process.env.GIT_AUTHOR_DATE);
        console.log("env:", process.env.NODE_ENV);
        new Game(gameConfig);
    });
}
