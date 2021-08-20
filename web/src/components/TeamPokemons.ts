import { Scene } from "phaser";
import { Pokemon } from "./Pokemon";

export const makeRedPokemon = (scene: Scene, name: string) =>
    new Pokemon(scene, {
        name,
        x: 70,
        y: 145,
        texture: withBack(name),
        depth: 1,
        nameTextX: 160,
        nameTextY: 100,
        scale: 1.6,
    });

export const makeBluePokemon = (scene: Scene, name: string) =>
    new Pokemon(scene, {
        name,
        x: 230,
        y: 50,
        texture: name,
        depth: 0,
        nameTextX: 25,
        nameTextY: 10,
        scale: 1.4,
    });

const withBack = (s: string) => `${s}back`;
