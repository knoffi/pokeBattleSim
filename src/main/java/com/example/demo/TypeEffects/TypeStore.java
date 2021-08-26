package com.example.demo.TypeEffects;

import com.example.demo.StoreButler.StoreButler;
import com.example.demo.StoreButler.StoreButlerServices;

public class TypeStore {
    static private TypeTable typeTable = getTypeTable();

    private static TypeTable getTypeTable() {
        try {
            TypeTable table = StoreButler.getData(StoreButlerServices.TYPES, TypeTable.class);
            return table;
        } catch (Exception e) {
            System.out.print("___GOT NOTHING FOR TYPE TABLE___" + e.getClass());
        }
        return new TypeTable();
    }

    public static Effectiveness getEffectiveness(String pokemonType, String attackType) {
        if (typeTable == null) {
            System.out.println("___NULL TYPE TABLE___");
            return Effectiveness.VERY;
        }
        return typeTable.getEffectiveness(pokemonType, attackType);
    }

}