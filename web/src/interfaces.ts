export interface IPokemon {
    name: string;
    frontSprite: string;
    backSprite: string;
}

export interface ApiRes {
    redTeam: IPokemon[];
    blueTeam: IPokemon[];
}
