package com.example.demo.Searches.PokemonSearch;

import com.example.demo.Pokedex.Pokedex;

public class VersionGroupDetailBySearch {
    public NameHolder version_group;

    public boolean isClassical() {
        return this.version_group.name.equals(Pokedex.CLASSICAL_VERSION_KEY);
    }
}