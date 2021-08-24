package com.example.demo.TypeEffects;

import java.util.Arrays;

class TypeData {
    private String typeName;
    private String[] doubleDamageTo;
    private String[] halfDamageTo;
    private String[] noDamageTo;

    TypeData() {

    }

    public Effectiveness getEffectivenessAgainstPokemon(String pokeType) {
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
        return type.equals(this.typeName);
    }

}