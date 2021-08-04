package com.example.demo.Searches.PokemonSearch;

import com.example.demo.Pokemon.Stat;

public class StatBySearch {
    public int base_stat;
    public NameHolder stat;

    StatBySearch() {
        this.base_stat = 0;
        this.stat = new NameHolder();
    }

    public Stat convert() {
        return new Stat(this.stat.name, this.base_stat);
    }
}
