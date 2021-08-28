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
        battleLog: string[];
    }>;
    blueWon: boolean;
}
