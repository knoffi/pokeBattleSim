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
        mode: Phaser.Scale.NONE,
        // OBS browser overlay dimensions
        width: 293, // gba screen width
        height: 198, // gba screen height
    },
};
