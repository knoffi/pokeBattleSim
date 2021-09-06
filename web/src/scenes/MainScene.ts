import { GameObjects, Scene } from "phaser";
import { Pokemon } from "../components/Pokemon";
import { makeBluePokemon, makeRedPokemon } from "../components/TeamPokemons";
import { ApiRes } from "../interfaces";
import { Color } from "../styles/Color";
import { TextConfig } from "../styles/TextConfig";
import { Scenes } from "./Scenes";

const cfg = {
    charactersPerLine: 19,
};

type Image = GameObjects.Image;
type AnimTimeline = Array<object | Phaser.Types.Tweens.TweenBuilderConfig>;

export class MainScene extends Scene {
    private battle!: ApiRes;
    private round!: number;
    private blue: Pokemon | undefined;
    private red: Pokemon | undefined;
    private mask!: Phaser.Display.Masks.GeometryMask;
    private text!: Phaser.GameObjects.Text;

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

        this.text = this.add
            .text(30, 150, "", {
                ...TextConfig.battleLog,
                wordWrap: { width: 250, useAdvancedWrap: true },
            })
            .setLineSpacing(10)
            .setMaxLines(2);

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

        const timeline = this.tweens.timeline();
        for (const event of round.battleLog) {
            const addToTimeline = (tween: AnimTimeline[0]) =>
                timeline.add(tween);
            const setTextInTimeline = (text: string) =>
                addToTimeline({
                    delay: 1000,
                    targets: [this.text],
                    x: this.text.x,
                    onStart: () => this.text.setText(text),
                });
            switch (event.type) {
                case "attack":
                    setTextInTimeline(event.message);
                    if (event.blueActs) {
                        this.attack(this.blue, this.red).forEach(addToTimeline);
                    } else {
                        this.attack(this.red, this.blue).forEach(addToTimeline);
                    }
                    break;
                case "faint":
                    const koPkmn = event.blueActs ? this.blue : this.red;
                    animKO(koPkmn).forEach(addToTimeline);
                    break;
                case "text":
                    addToTimeline({
                        delay: 1000,
                        targets: [this.text],
                        x: this.text.x, // phaser timeline's need to have some prop to be able to tween. otherwise onStart never gets called.
                        onStart: () => this.text.setText(event.message),
                    });
                    break;
                case "summon":
                    const pkmn = event.blueActs ? this.blue : this.red;
                    setTextInTimeline(event.message);
                    addToTimeline({
                        targets: [pkmn],
                        x: pkmn.x,
                        onStart: () => pkmn.setVisible(true),
                        duration: 500,
                    });
                    // TODO
                    // addToTimeline(animSummon(pkmn));
                    break;
            }
        }

        const onDefeat = (defender: Pokemon | undefined) => () => {
            defender?.deactivate();
            this.round++;
            this.events.emit("next round");
        };
        if (round.blueWon) {
            timeline.once("complete", onDefeat(this.red));
        } else {
            timeline.once("complete", onDefeat(this.blue));
        }
        timeline.play();
    }

    private attack(attacker: Image, defender: Image): AnimTimeline {
        // wait after spawning enemy, then attack
        return [
            {
                delay: 500,
                x: attacker.x - 0.8 * (attacker.x - defender.x), // linear combination
                y: attacker.y - 0.8 * (attacker.y - defender.y),
                scale: attacker.scale - 0.8 * (attacker.scale - defender.scale), // scale to indicate distance to camera, i.e. coming closer or moving away from the camera
                targets: [attacker],
                ease: "sine",
                duration: 250,
                yoyo: true,
            },
            // start blinking
            {
                delay: 100,
                alpha: 0,
                duration: 0,
                targets: [defender],
            },
            {
                delay: 100,
                alpha: 1,
                duration: 1,
                targets: [defender],
            },
            {
                delay: 100,
                alpha: 0,
                duration: 0,
                targets: [defender],
            },
            {
                delay: 100,
                alpha: 1,
                duration: 1,
                targets: [defender],
                completeDelay: 500,
            },
        ];
    }

    private nextBluePkmn(
        pokemon: string,
        mask: Phaser.Display.Masks.GeometryMask
    ) {
        const pkmn = makeBluePokemon(this, pokemon);
        pkmn.setMask(mask);
        pkmn.setVisible(false);
        return pkmn;
    }

    private nextRedPkmn(
        pokemon: string,
        mask: Phaser.Display.Masks.GeometryMask
    ) {
        const pkmn = makeRedPokemon(this, pokemon);
        pkmn.setMask(mask);
        pkmn.setVisible(false);
        return pkmn;
    }
}

const animKO = (defender: Image): AnimTimeline =>
    // start fall down
    [
        {
            delay: 300,
            y: 500,
            duration: 600,
            targets: [defender],
            completeDelay: 1500,
        },
    ];
