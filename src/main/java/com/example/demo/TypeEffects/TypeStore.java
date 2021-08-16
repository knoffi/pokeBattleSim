package com.example.demo.TypeEffects;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import com.example.demo.RequestMode;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.PokemonSearch.NameHolder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TypeStore {
    final static private String TYPE_FILE_PATH = "./pokeBattleSim/src/main/java/com/example/demo/TypeEffects/TypeTable.json";
    // BEWARE: api does not support checks for types form classical generation, thus
    // index array is hard coded
    // TODO: Test with api call whether we get the desired hard coded names by hard
    // coded index
    final static private int[] CLASSICAL_TYPE_INDICES = { 1, 2, 3, 4, 5, 6, 7, 8,
            /* path ending "9" is steel type, which is not classical */ 10, 11, 12, 13, 14, 15, 16 };

    public static void updateTypes() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        try {
            FileWriter file = new FileWriter(TYPE_FILE_PATH);
            TypeTable table = new TypeTable(getTypes());
            String data = mapper.writeValueAsString(table);
            file.write(data);
            file.close();

        } catch (IOException e) {
            System.out.println("___WRITING TYPE TABLE FAILED___" + e.getClass());
        }

    };

    private static TypeData[] getTypes() {
        TypeData[] types = new TypeData[15];
        for (int i = 0; i < CLASSICAL_TYPE_INDICES.length; i++) {
            int typeIndex = CLASSICAL_TYPE_INDICES[i];
            types[i] = getTypeData(typeIndex);
        }
        return types;
    }

    private static TypeData getTypeData(int typeIndex) {
        try {
            var types = Pokedex.getPokeData(Pokedex.API_PATH + "type/" + typeIndex + "/", TypeDataSearch.class,
                    RequestMode.JAVA_11);
            return types.convert();
        } catch (IOException | InterruptedException e) {
            System.out.println("___FAILING TYPE LOADING___" + e.getClass());
        }

        return new TypeData();
    }

    public static Effectiveness getEffectiveness(String pokemonType, String attackType) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        try {
            Path jsonPath = Paths.get(TYPE_FILE_PATH);
            TypeTable table = mapper.readValue(jsonPath.toFile(), TypeTable.class);
            return table.getEffectiveness(pokemonType, attackType);

        } catch (IOException e) {
            System.out.println("___FINDING TYPE TABLE FAILED___" + e.getClass());
        }
        return Effectiveness.NORMAL;

    }

}

class TypeTable {
    private TypeData[] types;

    TypeTable() {

    }

    TypeTable(TypeData[] types) {
        this.types = types;
    }

    void print() {
        System.out.println(this.types.length);
    }

    public Effectiveness getEffectiveness(String pokemonType, String attackType) {
        Optional<TypeData> typeRow = Arrays.stream(types).filter(type -> type.equals(attackType)).findAny();
        if (typeRow.isEmpty()) {
            try {
                throw new Exception("MissingPokeTypeRow");
            } catch (Throwable e) {
                System.out.println("___NO ROW FOR THIS ATTACK TYPE___" + e.getClass());
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
    private String typeName;
    private String[] doubleDamageFrom;
    private String[] doubleDamageTo;
    private String[] halfDamageFrom;
    private String[] halfDamageTo;
    private String[] noDamageFrom;
    private String[] noDamageTo;

    TypeData() {

    }

    public Effectiveness getEffectivenessAgainstPokemon(String pokeType) {
        boolean isEffective = Arrays.stream(this.doubleDamageTo).anyMatch(type -> type.equals(pokeType));
        if (isEffective) {
            return Effectiveness.EFFECTIVE;
        }
        boolean isHalfDmg = Arrays.stream(this.halfDamageTo).anyMatch(type -> type.equals(pokeType));
        if (isHalfDmg) {
            return Effectiveness.RESISTANT;
        }
        boolean isUseless = Arrays.stream(this.noDamageTo).anyMatch(type -> type.equals(pokeType));
        if (isUseless) {
            return Effectiveness.IMMUN;
        } else {
            return Effectiveness.NORMAL;
        }
    }

    public boolean equals(String type) {
        return type.equals(this.typeName);
    }

    TypeData(TypeDataSearch data) {
        this.doubleDamageFrom = data.damage_relations.getDamageNames(0);
        this.doubleDamageTo = data.damage_relations.getDamageNames(1);
        this.halfDamageFrom = data.damage_relations.getDamageNames(2);
        this.halfDamageTo = data.damage_relations.getDamageNames(3);
        this.noDamageFrom = data.damage_relations.getDamageNames(4);
        this.noDamageTo = data.damage_relations.getDamageNames(5);
        this.typeName = data.name;
    }
}

class TypesSearch {
    public NameHolder[] results;

    TypesSearch() {
    }
}