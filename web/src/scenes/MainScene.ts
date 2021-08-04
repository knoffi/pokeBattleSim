import { Scene } from "phaser";
import { Scenes } from "./Scenes";

const cfg = {};

export class MainScene extends Scene {
    public constructor() {
        super({
            key: Scenes.Main,
        });
    }

    public create(): void {
        // Do stuff
    }
}
