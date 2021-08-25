package com.example.demo.SupportedAttacks;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

import com.example.demo.JSONHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

}

class SupportedMoveCategories {
    private static String[] categories = { "damage", "damage+ailment", "damage+lower", "damage+raise",
            "net-good-stats" };

    public static boolean contain(String testCategory) {
        boolean testIsSupported = Arrays.stream(categories).anyMatch(category -> category.equals(testCategory));
        return testIsSupported;
    };
}

enum StoreButlerServices {
    ATTACKS("attacks");

    public final String key;

    StoreButlerServices(String key) {
        this.key = key;
    }
}

class StoreButler {
    /**
     *
     */
    private static final String WRAPPER_FIELD = "record";
    private static String responseBody = getResponseBody();
    private static ObjectMapper mapper = new ObjectMapper();

    private static String getResponseBody() {
        String headerField = "X-Master-Key";
        String testKey = "$2b$10$x6ODCTAqgUywRPunUmSpKOy.dcMwp/FnOmVl3RVF4ERQJsWf0B/Q2";
        try {
            var url = URI.create("https://api.jsonbin.io/v3/b/6125fd7ec5159b35ae0391be/latest");
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(url).header(headerField, testKey).build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            return responseBody;
        } catch (IOException | InterruptedException e) {
            System.out.println("___FAILED HTTP CALL TO JSONBIN.IO___" + e.getClass());
        }
        return "";
    }

    public static <T> T getData(StoreButlerServices service, Class<T> type) throws JsonProcessingException, Exception {
        JsonNode responseObject = mapper.readTree(responseBody);
        if (!responseObject.has(WRAPPER_FIELD)) {
            throw new Exception("ResponseHasNoRecordField");
        } else {
            if (!responseObject.get(WRAPPER_FIELD).has(service.key)) {
                throw new Exception("InvalidNodeKey");
            }
        }
        System.out.println(responseObject.toString());
        JsonNode typeObject = responseObject.get(WRAPPER_FIELD).get(service.key);
        T object = JSONHandler.convertJSON(typeObject.toString(), type);
        return object;

    }
}