import { Types } from "phaser";
import { DEV } from "./dev-config";
import { StandbyScene } from "./scenes/StandbyScene";

interface IPlugin {
    key: string;
    plugin: any;
}
const isPlugin = (x: false | IPlugin): x is IPlugin => !!x;
const DebugPlugins = [
    !!DEV.enableSceneWatcher && {
        key: "SceneWatcher",
        // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
        plugin: require("phaser-plugin-scene-watcher"),
    },
].filter(isPlugin);

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
    plugins: {
        global: [...DebugPlugins],
    },
    callbacks: {
        postBoot: (game) => {
            if (DEV.enableSceneWatcher) {
                (game.plugins.get("SceneWatcher") as any).watchAll();
            }
        },
    },
};
