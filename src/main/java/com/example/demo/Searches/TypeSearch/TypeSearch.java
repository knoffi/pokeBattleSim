package com.example.demo.Searches.TypeSearch;

import com.example.demo.Searches.PokemonSearch.NameHolder;
import com.example.demo.TypeEffects.TypeRow;

public class TypeSearch {
    public String name;
    public NameHolder generation;
    public DamageRelationsBySearch damage_relations;

    public TypeRow convert() {
        return new TypeRow(this);
    }

}
