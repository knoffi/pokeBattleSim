package com.example.demo.TypeEffects;

import java.util.Arrays;
import java.util.Optional;

public class TypeTable {
    private TypeRow[] types;

    TypeTable() {
    }

    TypeTable(TypeRow[] types) {
        this.types = types;
    }

    Effectiveness getEffectiveness(String pokemonType, String attackType) {
        Optional<TypeRow> typeRow = Arrays.stream(types).filter(type -> type.equals(attackType)).findAny();
        if (typeRow.isEmpty()) {
            try {
                throw new Exception("MissingPokeTypeRow");
            } catch (Throwable e) {
                System.out.println("___NO ROW FOR ATTACK TYPE " + attackType.toUpperCase() + "___" + e.getClass());
                return Effectiveness.NORMAL;
            }
        } else {
            TypeRow attackData = typeRow.get();
            return attackData.getEffectivenessAgainst(pokemonType);

        }
    }
}