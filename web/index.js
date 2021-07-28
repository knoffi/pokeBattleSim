// @ts-check

// TODO remove dev fixture for production
const isProduction = true;
const fixture = globalFixture;

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

const startBattle = document.querySelector("#start-battle-btn");

startBattle.addEventListener("click", async () => {
  /** @type {ApiRes} */
  const data = isProduction ? await fetch(url).then((x) => x.json()) : fixture;
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
