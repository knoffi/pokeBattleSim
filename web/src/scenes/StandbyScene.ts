import { Scene } from "phaser";
import { DEV, isProd } from "../dev-config";
import { ApiRes, IPokemon } from "../interfaces";
import { Color } from "../styles/Color";
import { MainScene } from "./MainScene";
import { Scenes } from "./Scenes";

const cfg = {
    minTimeShowingStartScreenInMs: 1500,
};

const isIPokemon = (x: unknown & Partial<IPokemon>) => {
    if (typeof x !== "object") {
        throw new Error(
            "Invalid response from backend. Team element is not an object"
        );
    }
    if (
        !x.backSprite ||
        !x.frontSprite ||
        typeof x.backSprite !== "string" ||
        typeof x.frontSprite !== "string" ||
        typeof x.name !== "string"
    ) {
        return false;
    }
    return true;
};

function assertApiRes(x: undefined | Partial<ApiRes>): asserts x is ApiRes {
    if (typeof x !== "object") {
        throw new Error("Invalid response from backend. Not an object");
    }
    const blue = x.blueTeam;
    const red = x.redTeam;
    if (!Array.isArray(red) || !Array.isArray(blue)) {
        throw new Error(
            "Invalid response from backend. At least one team is not an array"
        );
    }

    if (!blue.every(isIPokemon) || !red.every(isIPokemon)) {
        throw new Error(
            "Invalid response from backend. Team contains non-IPokemon"
        );
    }
}

const devUrl = "./assets/db.json";
const url =
    "https://raw.githubusercontent.com/knoffi/pokeBattleSim/main/db.json";

export class StandyScene extends Scene {
    constructor(key = Scenes.Standby) {
        super(key);
    }

    public preload() {
        this.load.image("gbastartscreen", "./assets/gba-startscreen.png");
    }

    public create() {
        this.cameras.main.setBackgroundColor(Color.SwitchedOffGrey);

        const startBattle = document.querySelector("#start-battle-btn");
        if (!startBattle) {
            throw new Error("Element not found by selector #start-battle-btn");
        }

        if (DEV.inBattleImmediately) {
            const api = isProd ? url : devUrl;
            const data = fetch(api)
                .then((x) => x.json() as Partial<ApiRes>)
                .then((data) => {
                    assertApiRes(data);
                    this.scene.add(Scenes.Main, MainScene, true, data);
                });
        }

        // eslint-disable-next-line @typescript-eslint/no-misused-promises
        startBattle.addEventListener("click", async () => {
            this.add.image(0, 0, "gbastartscreen").setOrigin(0).setScale(1.01); // remove 1-3px thin grey line on the right
            const beforeFetch = this.time.now;
            const api = isProd ? url : devUrl;
            const data = await fetch(api).then(
                (x) => x.json() as Partial<ApiRes>
            );
            assertApiRes(data);
            // NOTE: don't use scene.add without true + scene.start here, otherwise phaser will just ignore the start call for some reason. Bug?
            const startMainScene = () =>
                this.scene.add(Scenes.Main, MainScene, true, data);

            // show the start screen for at least minTimeShowingStartScreenInMs
            const minTimeHasNotPassedYet =
                beforeFetch + cfg.minTimeShowingStartScreenInMs > this.time.now;
            if (minTimeHasNotPassedYet) {
                this.time.delayedCall(
                    cfg.minTimeShowingStartScreenInMs -
                        (this.time.now - beforeFetch),
                    startMainScene,
                    undefined,
                    this
                );
            } else {
                startMainScene();
            }
        });
    }
}
