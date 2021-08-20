import { GameObjects, Scene } from "phaser";
import { Pokemon } from "../components/Pokemon";
import { makeBluePokemon, makeRedPokemon } from "../components/TeamPokemons";
import { ApiRes } from "../interfaces";
import { Color } from "../styles/Color";
import { Scenes } from "./Scenes";

const cfg = {};

export class MainScene extends Scene {
    private battle!: ApiRes;
    private round!: number;
    private blue: Pokemon | undefined;
    private red: Pokemon | undefined;
    private mask!: Phaser.Display.Masks.GeometryMask;

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
        // this.load.audio("attack", "./assets/sounds/attack_01.ogg");
        this.load.image("textbox", "./assets/textbox.nineslice.png");
    }

    public create(): void {
        this.round = 0;

        this.cameras.main.setBackgroundColor(Color.InBattleWhite);

        this.addTextboxWithMask();

        // enforce renders
        this.events.on("next round", () => this.nextRound());
        this.events.emit("next round");
    }

    private addTextboxWithMask() {
        const textbox = this.add.nineslice(0, 130, 70, 34, "textbox", 8);
        textbox.setScale(2).setDepth(1000);
        textbox.displayWidth = this.scale.width;
        const shape = this.make
            .graphics({
                x: 0,
                y: 130,
            })
            .fillRect(0, 0, this.scale.width, 100);
        this.mask = shape.createGeometryMask();
        this.mask.setInvertAlpha();
    }

    public shutdown() {
        this.events.destroy();
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
            : this.nextBluePkmn(round.blueCombatant, this.mask);
        this.red = this.red?.active
            ? this.red
            : this.nextRedPkmn(round.redCombatant, this.mask);

        const onDefeat = (defender: Pokemon | undefined) => () => {
            defender?.deactivate();
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
            // onYoyo: () => this.sound.play("attack"),
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

    private nextBluePkmn(
        pokemon: string,
        mask: Phaser.Display.Masks.GeometryMask
    ) {
        const pkmn = makeBluePokemon(this, pokemon);
        pkmn.setMask(mask);
        return pkmn;
    }

    private nextRedPkmn(
        pokemon: string,
        mask: Phaser.Display.Masks.GeometryMask
    ) {
        const pkmn = makeRedPokemon(this, pokemon);
        pkmn.setMask(mask);
        return pkmn;
    }
}
