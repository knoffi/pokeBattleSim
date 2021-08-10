import { Types } from "phaser";
import { StandbyScene } from "./scenes/StandbyScene";

export const gameConfig: Types.Core.GameConfig = {
    scene: StandbyScene,
    type: Phaser.AUTO,
    dom: {
        createContainer: true,
    },
    pixelArt: true,
    scale: {
        parent: "game",
        mode: Phaser.Scale.NONE,
        width: 293, // gba screen width
        height: 198, // gba screen height
    },
};
