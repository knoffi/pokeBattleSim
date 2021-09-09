package com.example.demo.TypeEffects;

import java.util.Arrays;

import com.example.demo.Searches.TypeSearch.TypeSearch;

public class TypeRow {
    private String name;
    private String[] doubleDamageTo;
    private String[] halfDamageTo;
    private String[] noDamageTo;

    public TypeRow() {

    }

    public TypeRow(TypeSearch typeSearch) {
        this.name = typeSearch.name;
        this.doubleDamageTo = Arrays.stream(typeSearch.damage_relations.double_damage_to).map(type -> type.name)
                .toArray(String[]::new);
        this.noDamageTo = Arrays.stream(typeSearch.damage_relations.no_damage_to).map(type -> type.name)
                .toArray(String[]::new);
        this.halfDamageTo = Arrays.stream(typeSearch.damage_relations.half_damage_to).map(type -> type.name)
                .toArray(String[]::new);
    }

    public Effectiveness getEffectivenessAgainst(String pokeType) {
        Effectiveness test = Effectiveness.NORMAL;
        boolean isEffective = Arrays.stream(this.doubleDamageTo).anyMatch(type -> type.equals(pokeType));
        if (isEffective) {

            test = Effectiveness.VERY;
        }
        boolean isHalfDmg = Arrays.stream(this.halfDamageTo).anyMatch(type -> type.equals(pokeType));
        if (isHalfDmg) {
            test = Effectiveness.RESISTANT;
        }
        boolean isUseless = Arrays.stream(this.noDamageTo).anyMatch(type -> type.equals(pokeType));
        if (isUseless) {
            test = Effectiveness.IMMUN;
        }

        return test;
    }

    public boolean equals(String type) {
        return type.equals(this.name);
    }

}