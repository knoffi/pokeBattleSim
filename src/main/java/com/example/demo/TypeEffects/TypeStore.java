package com.example.demo.TypeEffects;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.example.demo.JSONHandler;

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