import { Scene } from "phaser";
import { isProd } from "../dev-config";
import { ApiRes, IPokemon } from "../interfaces";
import { MainScene } from "./MainScene";
import { Scenes } from "./Scenes";

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

const devUrl =
    "https://raw.githubusercontent.com/proSingularity/pokeBattleSim/main/db.json";
const url =
    "https://raw.githubusercontent.com/knoffi/pokeBattleSim/main/db.json";

export class StandyScene extends Scene {
    constructor(key = Scenes.Standby) {
        super(key);
    }

    public create() {
        const startBattle = document.querySelector("#start-battle-btn");
        if (!startBattle) {
            throw new Error("Element not found by selector #start-battle-btn");
        }

        // eslint-disable-next-line @typescript-eslint/no-misused-promises
        startBattle.addEventListener("click", async () => {
            const api = isProd ? url : devUrl;
            const data = await fetch(api).then(
                (x) => x.json() as Partial<ApiRes>
            );
            assertApiRes(data);
            const teams = document.querySelector("#teams");
            if (!teams) {
                throw new Error("Element not found by selector #teams");
            }
            const redTeam = getTeamRow(data, "red");
            const blueTeam = getTeamRow(data, "blue");
            const vs = getVersusText();
            teams.append(redTeam, vs, blueTeam);
            const mainscene = this.scene.add(Scenes.Main, MainScene);
            this.scene.start(mainscene, data);
        });
    }
}

function getVersusText() {
    const vs = document.createElement("i");
    vs.className = "vs";
    vs.innerHTML = "VS";
    return vs;
}

function getTeamRow(data: ApiRes, color: "red" | "blue") {
    const team = document.createElement("div");
    team.className = "row";
    team.id = `${color}-team`;
    const teamData = data[`${color}Team` as const];

    const pkmnsAsRows = teamData.map((pkmn) => {
        const pkmnNode = document.createElement("div");
        pkmnNode.className = `col image-with-caption`;
        const sprite = document.createElement("img");
        sprite.src = pkmn.frontSprite;
        sprite.alt = pkmn.name;

        const caption = document.createElement("p");
        caption.innerHTML = pkmn.name;

        pkmnNode.append(sprite, caption);

        return pkmnNode;
    });

    team.append(...pkmnsAsRows);
    return team;
}
