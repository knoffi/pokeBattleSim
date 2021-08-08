import { GUI } from "dat.gui";
import { Scene } from "phaser";
import { ApiRes } from "../interfaces";
import { Color } from "../styles/Color";
import { Scenes } from "./Scenes";

const cfg = {};

export class MainScene extends Scene {
    private battle!: ApiRes;

    public constructor() {
        super({
            key: Scenes.Main,
        });
    }

    public init(data: ApiRes) {
        this.battle = data;
    }

    public preload() {
        [...this.battle.blueTeam, ...this.battle.redTeam].forEach((pkmn) =>
            this.load
                .image(`${pkmn.name}back`, pkmn.backSprite)
                .image(pkmn.name, pkmn.frontSprite)
        );
    }

    public create(): void {
        const gui = new GUI();

        this.cameras.main.setBackgroundColor(Color.SwitchedOffGrey);

        const blue = this.nextBluePkmn(0);
        const red = this.nextRedPkmn(0);

        const i = 0;
        if (this.battle.rounds[i].blueWon) {
            const timeline = this.tweens.createTimeline();
            timeline.add({
                x: blue.x - 0.8 * (blue.x - red.x), // linear combination
                y: blue.y - 0.8 * (blue.y - red.y),
                targets: [blue],
                ease: "sine",
                duration: 250,
                yoyo: true,
            });
            timeline.add({ delay: 100, alpha: 0, duration: 0, targets: [red] });
            timeline.add({ delay: 100, alpha: 1, duration: 1, targets: [red] });
            timeline.add({ delay: 100, alpha: 0, duration: 0, targets: [red] });
            timeline.add({ delay: 100, alpha: 1, duration: 1, targets: [red] });
            timeline.add({
                delay: 200,
                y: 500,
                duration: 200,
                targets: [red],
            });
            timeline.play();
        }
        // TODO remove test images
        // Do stuff
    }

    private nextBluePkmn(index: number) {
        const blue = this.add
            .image(230, 50, this.battle.blueTeam[index].name)
            .setScale(1.6);
        return blue;
    }

    private nextRedPkmn(index: number) {
        const red = this.add
            .image(70, 145, withBack(this.battle.redTeam[0].name))
            .setScale(1.6);
        return red;
    }
}

const withBack = (s: string) => `${s}back`;
