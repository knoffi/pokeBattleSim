package com.example.demo.SupportedAttacks;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

import com.example.demo.JSONHandler;
import com.example.demo.RequestMode;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Pokemon.Attack;
import com.example.demo.Searches.MoveSearch.MoveNameHolders;
import com.example.demo.Searches.MoveSearch.MoveSearch;
import com.example.demo.Searches.PokemonSearch.NameHolder;

public class AttackStore {
    private final static String SUPPORTED_ATTACKS_FILE_PATH = "http://localhost:8080/supportedAttackNames.json";

    private static String[] loadSupportedNames() {
        try {
            var url = URI.create(SUPPORTED_ATTACKS_FILE_PATH);
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(url).build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            String[] list = JSONHandler.convertJSON(responseBody, String[].class);
            return list;
        } catch (IOException | InterruptedException e) {
            System.out.println("___GET REQUEST FOR  SUPPORTED ATTACKS FAILEd" + e.getClass());
        }
        System.out.println("We only support the attack move struggle...");
        String[] defaultResult = { "struggle" };
        return defaultResult;
    }

    public final static String[] SUPPORTED_ATTACKS_BY_NAME = loadSupportedNames();

    private static Attack getAttackFromUrl(String url) {
        try {
            String path = Pokedex.getPathFromURL(url);
            Attack attack = Pokedex.getPokeData(path, MoveSearch.class, RequestMode.JAVA_11).convert();
            return attack;
        } catch (IOException | InterruptedException | NullPointerException e) {
            System.out.println("___ERROR IN GETTING ATTACK FROM URL___" + e.getClass());
        }
        return new Attack();
    }

    private static Attack[] getClassicalAttacks() {
        try {
            NameHolder[] nameHolders = Pokedex.getPokeData(Pokedex.API_PATH + Pokedex.GENERATION_I_PATH,
                    MoveNameHolders.class, RequestMode.JAVA_11).moves;
            Attack[] attacks = Arrays.stream(nameHolders).map(nameHolder -> getAttackFromUrl(nameHolder.url))
                    .toArray(Attack[]::new);
            return attacks;
        } catch (IOException | InterruptedException | NullPointerException e) {
            System.out.println("___PROBLEM AT LOADING ATTACKS___" + e.getClass());
        }

        return new Attack[0];
    }

    private static Attack[] getSupportedAttacks(Attack[] attacks) {
        return Arrays.stream(attacks).filter(attack -> SupportedMoveCategories.contain(attack.getCategory()))
                .toArray(Attack[]::new);
    }

    public static void update() throws IOException {
        FileWriter fileWriter = new FileWriter(SUPPORTED_ATTACKS_FILE_PATH);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        String supportedAttackNames = "";
        Attack[] classicalAttacks = getClassicalAttacks();
        Attack[] supportedAttacks = getSupportedAttacks(classicalAttacks);
        for (int i = 0; i < supportedAttacks.length; i++) {
            supportedAttackNames += supportedAttacks[i].getName() + " ";
        }
        printWriter.print(supportedAttackNames);
        printWriter.close();
    }

    public static boolean isSupported(String name) {
        return Arrays.stream(SUPPORTED_ATTACKS_BY_NAME).anyMatch(attackName -> attackName.equals(name));
    }
}

class SupportedMoveCategories {
    private static String[] categories = { "damage", "damage+ailment", "damage+lower", "damage+raise",
            "net-good-stats" };

    public static boolean contain(String testCategory) {
        boolean testIsSupported = Arrays.stream(categories).anyMatch(category -> category.equals(testCategory));
        return testIsSupported;
    };
}
