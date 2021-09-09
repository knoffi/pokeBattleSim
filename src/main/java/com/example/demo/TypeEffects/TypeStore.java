package com.example.demo.TypeEffects;

import java.io.IOException;
import java.util.Arrays;

import com.example.demo.RequestMode;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.TypeSearch.TypeSearch;
import com.example.demo.Searches.TypesSearch.TypesSearch;
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

    public static TypeTable getUpdatedTable() {
        try {
            TypesSearch types = Pokedex.getPokeData(Pokedex.API_PATH + Pokedex.TYPE_PATH, TypesSearch.class,
                    RequestMode.JAVA_11);
            TypeRow[] typeRows = Arrays.stream(types.results).map(type -> getTypeFromURL(type.url).convert())
                    .toArray(TypeRow[]::new);
            return new TypeTable(typeRows);

        } catch (IOException | InterruptedException e) {
            System.out.print("___FAIL AT GETTING RELEVANT MOVES___");
        }
        TypeTable defaultTable = new TypeTable();
        return defaultTable;
    }

    private static TypeSearch getTypeFromURL(String url) {
        try {
            return Pokedex.getPokeDataByURL(url, TypeSearch.class, RequestMode.JAVA_11);
        } catch (IOException | InterruptedException e) {
            System.out.println("___FAIL AT GETTING MOVE " + url + "___");
            return new TypeSearch();
        }

    }

}