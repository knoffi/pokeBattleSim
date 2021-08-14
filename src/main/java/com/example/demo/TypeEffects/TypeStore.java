package com.example.demo.TypeEffects;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.RequestMode;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.PokemonSearch.NameHolder;

import org.json.simple.JSONObject;

public class TypeStore {
    final static private String TYPE_FILE_PATH = "./pokeBattleSim/src/main/java/com/example/demo/TypeEffects/TypeTable.json";

    public static void updateTypes() {
        JSONObject test = new JSONObject(getTypeData(1));
        try {
            FileWriter file = new FileWriter(TYPE_FILE_PATH);
            file.write(test.toJSONString());
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

    private static Map<String, String[]> getTypeData(int typeIndex) {
        try {
            var types = Pokedex.getPokeData(Pokedex.API_PATH + "type/" + typeIndex + "/", TypeDataSearch.class,
                    RequestMode.JAVA_11).getDamageMap();
            return types;
        } catch (IOException | InterruptedException e) {
            System.out.println("___FAILING TYPE LOADING___" + e.getClass());
        }

        return new HashMap<>();
    }
}

class TypeTable {
    private NameHolder[] types;

    TypeTable(NameHolder[] types) {
        this.types = types;
    }
}

class TypeDataSearch {
    public DamageRelationsBySearch damage_relations;
    public String name;

    public Map<String, String[]> getDamageMap() {
        Map<String, String[]> damageMap = new HashMap();
        System.out.println(this.damage_relations);
        System.out.println("okay");
        damageMap.put("double_damage_from", this.damage_relations.getDamageName(0));
        damageMap.put("double_damage_to", this.damage_relations.getDamageName(1));
        damageMap.put("half_damage_from", this.damage_relations.getDamageName(2));
        damageMap.put("half_damage_to", this.damage_relations.getDamageName(3));
        damageMap.put("no_damage_from", this.damage_relations.getDamageName(4));
        damageMap.put("no_damage_to", this.damage_relations.getDamageName(5));
        return damageMap;
    }

}

class TypesSearch {
    public NameHolder[] results;

    TypesSearch() {
    }
}