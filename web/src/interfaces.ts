export interface IPokemon {
    name: string;
    frontSprite: string;
    backSprite: string;
}

export interface ApiRes {
    redTeam: IPokemon[];
    blueTeam: IPokemon[];
    rounds: Array<{
        redCombatant: string;
        blueCombatant: string;
        blueWon: boolean;
        battleLog: BattleEvent[];
    }>;
    blueWon: boolean;
}

type BattleEvent = AttackEvent | FaintEvent | TextEvent | SummonEvent;

export type AttackType =
    | "water"
    | "electric"
    | "grass"
    | "fire"
    | "bug"
    | "normal"
    | "fighting"
    | "psychic"
    | "flying"
    | "poison"
    | "ground"
    | "rock"
    | "ghost"
    | "dragon"
    | "ice"
    | "statusChange";

export interface AttackEvent {
    type: "attack";
    blueActs: boolean;
    attackType: AttackType;
    message: string;
}
interface TextEvent {
    type: "text";
    message: string;
}

interface FaintEvent {
    type: "faint";
    blueActs: boolean;
    message: string;
}

interface SummonEvent {
    type: "summon";
    blueActs: boolean;
    message: string;
}
