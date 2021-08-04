import { Types } from "phaser";
import { StandyScene } from "./scenes/StandbyScene";

export const gameConfig: Types.Core.GameConfig = {
    scene: StandyScene,
    type: Phaser.AUTO,
    dom: {
        createContainer: true,
    },
    physics: {
        default: "arcade",
        arcade: {
            debug: false,
        },
    },
    pixelArt: true,
    scale: {
        parent: "game",
        mode: Phaser.Scale.FIT,
        autoCenter: Phaser.Scale.CENTER_BOTH,
        // OBS browser overlay dimensions
        width: 1440,
        height: 1080,
        // width: window.innerWidth * window.devicePixelRatio,
        // height: window.innerHeight * window.devicePixelRatio,
    },
};
