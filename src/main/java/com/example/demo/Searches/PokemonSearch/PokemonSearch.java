package com.example.demo.Searches.PokemonSearch;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Pokemon.Pokemon;

public class PokemonSearch {
    public String name;
    public StatBySearch[] stats;
    public TypeHolder[] types;
    public MoveBySearch[] moves;
    public SpritesBySearch sprites;

    public Pokemon convert(Languages language) {
        return new Pokemon(this, language);
    }
}