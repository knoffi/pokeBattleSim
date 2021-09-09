package com.example.demo.StoreButler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;

import com.example.demo.JSONHandler;
import com.example.demo.Combat.PhraseStore.PhraseStore;
import com.example.demo.Combat.PhraseStore.PhraseTable;
import com.example.demo.SupportedAttacks.AttackStore;
import com.example.demo.TypeEffects.TypeStore;
import com.example.demo.TypeEffects.TypeTable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StoreButler {

    private static final String WRAPPER_FIELD = "record";
    private static final String HEADER_FIELD = "X-Master-Key";
    private static final String JSON_BIN_URL = "https://api.jsonbin.io/v3/b/61265c142aa80036126f7f7b/latest";
    private static final String JSON_BIN_API_KEY = System.getenv("JSON_BIN_API_KEY");
    private static ObjectMapper MAPPER = new ObjectMapper();
    private static String RESPONSE_BODY = getResponseBody();

    private static String getResponseBody() {
        try {
            var url = URI.create(JSON_BIN_URL);
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(url).header(HEADER_FIELD, JSON_BIN_API_KEY).build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            return responseBody;
        } catch (IOException | InterruptedException e) {
            System.out.println("___FAILED HTTP CALL TO JSONBIN.IO___" + e.getClass());
        }
        return "";
    }

    public static <T> T getData(StoreButlerServices service, Class<T> type) throws JsonProcessingException, Exception {
        JsonNode responseObject = MAPPER.readTree(RESPONSE_BODY);
        if (!responseObject.has(WRAPPER_FIELD)) {
            throw new Exception("ResponseHasNoRecordField");
        } else {
            if (!responseObject.get(WRAPPER_FIELD).has(service.key)) {
                throw new Exception("InvalidNodeKey");
            }
        }
        JsonNode typeObject = responseObject.get(WRAPPER_FIELD).get(service.key);
        T object = JSONHandler.convertJSON(typeObject.toString(), type);
        return object;

    }

    public static void sendUpdatesToCloud() {

        var url = URI.create("https://api.jsonbin.io/b/61265c142aa80036126f7f7b/");
        var client = HttpClient.newHttpClient();
        try {

            BodyPublisher body = BodyPublishers.ofString(JSONHandler.getNiceString(getUpdatedNotes()));
            Builder request = HttpRequest.newBuilder(url).PUT(body);

            request.header("Content-Type", "application/json");
            request.header("secret-key", JSON_BIN_API_KEY);
            request.header("versioning", "false");

            try {
                client.send(request.build(), HttpResponse.BodyHandlers.ofString());
            } catch (InterruptedException | IOException e) {
                System.out.print("___FAIL AT SENDING UPDATES TO CLOUD___" + e.getClass());
            }
        } catch (JsonProcessingException e) {
            System.out.println("___FAIL AT GETTING STRING FROM NOTES___");
        }
    }

    private static ButlerNotes getUpdatedNotes() {
        String[] attacks = AttackStore.getUpdatedNames();
        TypeTable types = TypeStore.getUpdatedTable();
        PhraseTable phrases = PhraseStore.getUpdatedTable();

        ButlerNotes updatedNotes = new ButlerNotes(attacks, types, phrases);
        return updatedNotes;

    }
}