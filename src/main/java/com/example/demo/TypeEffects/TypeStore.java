package com.example.demo.TypeEffects;

import java.io.FileWriter;
import java.io.IOException;

import com.example.demo.RequestMode;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.PokemonSearch.NameHolder;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TypeStore {
    final static private String TYPE_FILE_PATH = "./pokeBattleSim/src/main/java/com/example/demo/TypeEffects/TypeTable.json";

    public static void updateTypes() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        try {
            FileWriter file = new FileWriter(TYPE_FILE_PATH);
            String data = mapper.writeValueAsString(getTypeData(1));
            file.write(data);
            file.close();
            NameHolder[] types = getTypes();

        } catch (IOException e) {
            System.out.println("___WRITING TYPE TABLE FAILED___" + e.getClass());
        }

    };

    private static NameHolder[] getTypes() {
        try {
            NameHolder[] types = Pokedex.getPokeData(Pokedex.API_PATH + Pokedex.CLASSICAL_TYPES_PATH, TypesSearch.class,
                    RequestMode.JAVA_11).results;
            return types;
        } catch (IOException | InterruptedException e) {
            System.out.println("___FAILING TYPE LOADING___" + e.getClass());
        }

        return new NameHolder[0];
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
}

class TypeTable {
    private TypeData[] types;

    TypeTable(TypeData[] types) {
        this.types = types;
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