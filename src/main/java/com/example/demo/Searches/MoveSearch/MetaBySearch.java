package com.example.demo.Searches.MoveSearch;

import com.example.demo.Pokemon.Meta;
import com.example.demo.Searches.PokemonSearch.NameHolder;

public class MetaBySearch {
    public NameHolder ailment;
    public NameHolder category;
    public int ailment_chance;
    public int crit_rate;
    public int flinch_chance;
    public int drain;
    public int healing;
    public int stat_chance;
    public int max_hits;
    public int min_hits;
    public int max_turns;
    public int min_turns;

    public Meta convert() {
        return new Meta(this);
    }

}
