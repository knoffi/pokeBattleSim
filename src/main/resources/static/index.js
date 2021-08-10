// @ts-nocheck

// TODO remove dev fixture for production
// @ts-ignore
const isProduction = false;

// @ts-ignore
const devUrl =
  "https://raw.githubusercontent.com/proSingularity/pokeBattleSim/main/db.json";
// @ts-ignore
const url =
  "https://raw.githubusercontent.com/knoffi/pokeBattleSim/main/db.json";

/**
 * @typedef {{
 *  name: string,
 *  frontSprite: string;
 * }} IPokemon
 */

/**
 * @typedef {{
 *  redTeam: IPokemon[],
 *  blueTeam: IPokemon[],
 * }} ApiRes
 */

// @ts-ignore
const startBattle = document.querySelector("#start-battle-btn");

startBattle.addEventListener("click", async () => {
  const api = isProduction ? url : devUrl;
  /** @type {ApiRes} */
  const data = await fetch(api).then((x) => x.json());
  const teams = document.querySelector("#teams");
  const redTeam = getTeamRow(data, "red");
  const blueTeam = getTeamRow(data, "blue");
  const vs = getVersusText();
  teams.append(redTeam, vs, blueTeam);
});

function getVersusText() {
  const vs = document.createElement("i");
  vs.className = "vs";
  vs.innerHTML = "VS";
  return vs;
}

/**
 * @param {ApiRes} data
 * @param {'red' | 'blue'} color
 */
function getTeamRow(data, color) {
  const team = document.createElement("div");
  team.className = "row";
  team.id = `${color}-team`;

  /** @type {HTMLDivElement[]} */
  const pkmnsAsRows = data[`${color}Team`].map(
    /** @param {ApiRes['blueTeam'][0] | ApiRes['redTeam'][0]} pkmn */
    (pkmn) => {
      const pkmnNode = document.createElement("div");
      pkmnNode.className = `col image-with-caption`;
      const sprite = document.createElement("img");
      sprite.src = pkmn.frontSprite;
      sprite.alt = pkmn.name;

      const caption = document.createElement("p");
      caption.innerHTML = pkmn.name;

      pkmnNode.append(sprite, caption);

      return pkmnNode;
    }
  );

  team.append(...pkmnsAsRows);
  return team;
}
