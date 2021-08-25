package com.example.demo.SupportedAttacks;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.example.demo.JSONHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AttackStore {
    private final static String SUPPORTED_ATTACKS_FILE_PATH = "http://localhost:8080/supportedAttackNames.json";
    private static String[] SUPPORTED_ATTACKS_BY_NAME = { "blizzard", "solar beam" };// loadSupportedNames();

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
            System.out.println("___GET REQUEST FOR  SUPPORTED ATTACKS FAILED" + e.getClass());
        }
        System.out.println("We only support the attack move struggle...");
        String[] defaultResult = { "struggle" };
        return defaultResult;
    }

    public static boolean isSupported(String name) {
        return Arrays.stream(SUPPORTED_ATTACKS_BY_NAME).anyMatch(attackName -> attackName.equals(name));
    }

    public static void testUpdate() {
        try {
            List<String> books = Arrays.asList("bete", "barby");

            ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(Paths.get("pokeBattleSim/src/main/resources/static/books.json").toFile(), books);

        } catch (Exception e) {
            System.out.println(e);
        }
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
