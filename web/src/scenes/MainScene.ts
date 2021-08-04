import { Scene } from "phaser";
import { ApiRes } from "../interfaces";
import { Scenes } from "./Scenes";

const cfg = {};

export class MainScene extends Scene {
    private initData!: ApiRes;

    public constructor() {
        super({
            key: Scenes.Main,
        });
    }

    public init(data: ApiRes) {
        this.initData = data;
    }

    public preload() {
        [...this.initData.blueTeam, ...this.initData.redTeam].forEach((pkmn) =>
            this.load
                .image(`${pkmn.name}back`, pkmn.backSprite)
                .image(pkmn.name, pkmn.frontSprite)
        );
    }

    public create(): void {
        const gameElement =
            document.querySelector<HTMLDivElement>(".gamecontainer")!;
        gameElement.style.display = "block";

        this.add.image(100, 100, this.initData.blueTeam[3].name);
        this.add.image(300, 200, withBack(this.initData.blueTeam[3].name));
        // Do stuff
    }
}

const withBack = (s: string) => `${s}back`;
