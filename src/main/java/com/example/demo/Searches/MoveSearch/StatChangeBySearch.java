package com.example.demo.Searches.MoveSearch;

import com.example.demo.Pokemon.StatChange;
import com.example.demo.Searches.PokemonSearch.NameHolder;

public class StatChangeBySearch {
    public int change;
    public NameHolder stat;

    public StatChange convert() {
        return new StatChange(this.change, this.stat.name);
    }
}
