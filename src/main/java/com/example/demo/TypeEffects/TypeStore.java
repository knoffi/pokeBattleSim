package com.example.demo.TypeEffects;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Optional;

import com.example.demo.JSONHandler;
import com.example.demo.Searches.PokemonSearch.NameHolder;

public class TypeStore {
    final static private String TYPE_FILE_PATH = "http://localhost:8080/TypeTable.json";
    static private TypeTable typeTable = getTypeTable();

    private static TypeTable getTypeTable() {
        try {
            var url = URI.create(TYPE_FILE_PATH);
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(url).build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            TypeTable table = JSONHandler.convertJSON(responseBody, TypeTable.class);
            if (table == null) {
                System.out.println("___TYPE TABLE IS NULL___");
                return new TypeTable();

            }
            return table;
        } catch (IOException | InterruptedException e) {
            System.out.print("___GET REQUEST FOR TYPE TABLE FAILED___" + e.getClass());
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

class TypeTable {
    public TypeData[] types;

    TypeTable() {
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

class TypeDataSearch {
    public DamageRelationsBySearch damage_relations;
    public String name;

    public TypeData convert() {
        return new TypeData(this);
    }

}

class TypeData {
    public String typeName;
    public String[] doubleDamageTo;
    public String[] halfDamageTo;
    public String[] noDamageTo;

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

    TypeData(TypeDataSearch data) {
        this.doubleDamageTo = data.damage_relations.getDamageNames(1);
        this.halfDamageTo = data.damage_relations.getDamageNames(3);
        this.noDamageTo = data.damage_relations.getDamageNames(5);
        this.typeName = data.name;
    }
}

class TypesSearch {
    public NameHolder[] results;

    TypesSearch() {
    }
}