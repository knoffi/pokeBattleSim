package com.example.demo.StoreButler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.example.demo.JSONHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StoreButler {

    private static final String WRAPPER_FIELD = "record";
    private static final String HEADER_FIELD = "X-Master-Key";
    private static final String JSON_BIN_URL = "https://api.jsonbin.io/v3/b/61264e27c5159b35ae03b301/latest";
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
}