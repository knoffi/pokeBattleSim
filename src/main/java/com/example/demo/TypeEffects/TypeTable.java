package com.example.demo.TypeEffects;

import java.util.Arrays;
import java.util.Optional;

class TypeTable {
    private TypeData[] types;

    TypeTable() {
    }

    TypeTable(TypeData[] types) {
        this.types = types;
    }

    public Effectiveness getEffectiveness(String pokemonType, String attackType) {
        Optional<TypeData> typeRow = Arrays.stream(types).filter(type -> type.equals(attackType)).findAny();
        if (typeRow.isEmpty()) {
            try {
                throw new Exception("MissingPokeTypeRow");
            } catch (Throwable e) {
                System.out.println("___NO ROW FOR ATTACK TYPE " + attackType.toUpperCase() + "___" + e.getClass());
                return Effectiveness.NORMAL;
            }
        } else {
            TypeData attackData = typeRow.get();
            return attackData.getEffectivenessAgainstPokemon(pokemonType);

        }
    }
}