import { GUI } from "dat.gui";
import { GameObjects, Scene } from "phaser";
import { ApiRes } from "../interfaces";
import { Color } from "../styles/Color";
import { Scenes } from "./Scenes";

const cfg = {};

export class MainScene extends Scene {
    private battle!: ApiRes;
    private round!: number;
    private blue: GameObjects.Image | undefined;
    private red: GameObjects.Image | undefined;

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
        this.round = 0;

        const gui = new GUI();

        this.cameras.main.setBackgroundColor(Color.InBattleWhite);

        // enforce renders
        this.events.on("next round", () => this.nextRound());
        this.events.emit("next round");
    }

    /** recursively calling this method via event. Anchor on all rounds having been displayed. */
    private nextRound() {
        const finalRoundEnded = this.round >= this.battle.rounds.length;
        if (finalRoundEnded) {
            return;
        }

        const round = this.battle.rounds[this.round];
        this.blue = this.blue?.active
            ? this.blue
            : this.nextBluePkmn(round.blueCombatant);
        this.red = this.red?.active
            ? this.red
            : this.nextRedPkmn(round.redCombatant);

        const onDefeat = (defender: GameObjects.Image | undefined) => () => {
            defender?.setActive(false).setVisible(false);
            this.round++;
            this.events.emit("next round");
        };
        if (round.blueWon) {
            const timeline = this.attackAndDie(this.blue, this.red);
            timeline.once("complete", onDefeat(this.red));
        } else {
            const timeline = this.attackAndDie(this.red, this.blue);
            timeline.once("complete", onDefeat(this.blue));
        }
    }

    private attackAndDie(
        attacker: GameObjects.Image,
        defender: GameObjects.Image
    ) {
        const timeline = this.tweens.createTimeline();
        // wait after spawning enemy, then attack
        timeline.add({
            delay: 500,
            x: attacker.x - 0.8 * (attacker.x - defender.x), // linear combination
            y: attacker.y - 0.8 * (attacker.y - defender.y),
            scale: attacker.scale - 0.8 * (attacker.scale - defender.scale), // scale to indicate distance to camera, i.e. coming closer or moving away from the camera
            targets: [attacker],
            ease: "sine",
            duration: 250,
            yoyo: true,
        });
        // start blinking
        timeline.add({
            delay: 100,
            alpha: 0,
            duration: 0,
            targets: [defender],
        });
        timeline.add({
            delay: 100,
            alpha: 1,
            duration: 1,
            targets: [defender],
        });
        timeline.add({
            delay: 100,
            alpha: 0,
            duration: 0,
            targets: [defender],
        });
        timeline.add({
            delay: 100,
            alpha: 1,
            duration: 1,
            targets: [defender],
        });
        // end blinking
        // start fall down
        timeline.add({
            delay: 200,
            y: 500,
            duration: 200,
            targets: [defender],
        });
        timeline.play();
        return timeline;
    }

    private nextBluePkmn(pokemon: string) {
        return this.add.image(230, 50, pokemon).setScale(1.4);
    }

    private nextRedPkmn(pokemon: string) {
        return this.add
            .image(70, 145, withBack(pokemon))
            .setScale(1.6) // slightly larger than blue for perspective
            .setDepth(1);
    }
}

const withBack = (s: string) => `${s}back`;
