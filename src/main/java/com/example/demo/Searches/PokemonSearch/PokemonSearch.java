package com.example.demo.Searches.PokemonSearch;

import com.example.demo.Pokemon.Pokemon;

public class PokemonSearch {
    public String name;
    public StatBySearch[] stats;
    public TypeHolder[] types;
    public MoveBySearch[] moves;
    public SpritesBySearch sprites;

    public Pokemon convert() {
        return new Pokemon(this);
    }
}