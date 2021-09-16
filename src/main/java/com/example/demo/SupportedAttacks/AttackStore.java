package com.example.demo.SupportedAttacks;

import java.io.IOException;
import java.util.Arrays;

import com.example.demo.RequestMode;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.MoveSearch.MoveSearch;
import com.example.demo.Searches.MovesSearch.MovesSearch;
import com.example.demo.StoreButler.StoreButler;
import com.example.demo.StoreButler.StoreButlerServices;

public class AttackStore {
    private static String[] SUPPORTED_ATTACKS_BY_NAME = loadSupportedNames();

    private static String[] loadSupportedNames() {
        try {
            String[] attackNames = StoreButler.getData(StoreButlerServices.ATTACKS, String[].class);
            return attackNames;
        } catch (Exception e) {
            System.out.println("___ATTACK STORE GOT NOTHING___" + e.getClass());
        }
        System.out.println("We only support the attack move struggle...");
        String[] defaultResult = { "struggle" };
        return defaultResult;
    }

    public static boolean isSupported(String name) {
        return Arrays.stream(SUPPORTED_ATTACKS_BY_NAME).anyMatch(attackName -> attackName.equals(name));
    }

    public static String[] getUpdatedNames() {
        try {
            MovesSearch moves = Pokedex.getPokeData(Pokedex.API_PATH + Pokedex.RELEVANT_ATTACKS, MovesSearch.class,
                    RequestMode.JAVA_11);
            String[] attacks = Arrays.stream(moves.results).map(move -> getMoveFromURL(move.url))
                    .filter(move -> GenerationFilter.check(move.generation.name))
                    .filter(move -> CategoryFilter.check(move.meta.category.name, move.name))
                    .map(moveSearch -> moveSearch.name).toArray(String[]::new);
            return attacks;

        } catch (IOException | InterruptedException e) {
            System.out.print("___FAIL AT GETTING RELEVANT MOVES___");
        }
        ;
        String[] defaultResult = { "struggle" };
        return defaultResult;
    }

    private static MoveSearch getMoveFromURL(String url) {
        try {
            return Pokedex.getPokeDataByURL(url, MoveSearch.class, RequestMode.JAVA_11);
        } catch (IOException | InterruptedException e) {
            System.out.println("___FAIL AT GETTING MOVE " + url + "___");
            return new MoveSearch();
        }

    }
}

class CategoryFilter {
    private static String[] categories = { "damage", "damage+ailment", "damage+lower", "damage+raise", "net-good-stats",
            "ailment" };

    public static boolean check(String testCategory, String name) {
        return !name.equals("leech-seed")
                && Arrays.stream(categories).anyMatch(category -> category.equals(testCategory));
    };
}

class GenerationFilter {
    public static boolean check(String generation) {
        return generation.equals("generation-i");
    }
}