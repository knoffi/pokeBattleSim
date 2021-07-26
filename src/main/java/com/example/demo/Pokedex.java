package com.example.demo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Pokedex {
    private static int CLASSICAL_POKEMON_RANGE = 151;
    private static String CLASSICAL_POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/?limit=" + CLASSICAL_POKEMON_RANGE
            + "/";

    public static List<String> getClassicalPokemons(HTTPMode mode) throws IOException, InterruptedException {
        if (mode == HTTPMode.JAVA_11) {
            return getClassicalPokemonModernly();
        } else {
            return getClassicalPokemonRobustly();
        }
    }

    private static List<String> getClassicalPokemonModernly() throws IOException, InterruptedException {
        URI pokemonURL = URI.create(CLASSICAL_POKEMON_URL);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(pokemonURL).build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        Pokemon[] pokemons = convertJSON(responseBody);

        return getNames(pokemons);

    }

    private static List<String> getClassicalPokemonRobustly() throws RuntimeException, IOException {
        List<String> names = new ArrayList<String>();
        URL pokemonsURL = new URL(CLASSICAL_POKEMON_URL);
        HttpURLConnection connection = (HttpURLConnection) pokemonsURL.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Request failed: Http Error " + responseCode);
        }
        String responseString = "";
        Scanner scanner = new Scanner(pokemonsURL.openStream());
        while (scanner.hasNext()) {
            responseString += scanner.nextLine();
        }
        Pokemon[] pokemons = convertJSON(responseString);
        for (Pokemon pokemon : pokemons) {
            names.add(pokemon.name);
        }
        scanner.close();
        connection.disconnect();
        return getNames(pokemons);

    }

    private static Pokemon[] convertJSON(String pokemonResponse) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Pokemon[] pokemons = mapper.readValue(pokemonResponse, SearchAnswer.class).results;
        return pokemons;
    }

    private static ArrayList<String> getNames(Pokemon[] pokemons) {
        ArrayList<String> names = new ArrayList<String>();
        for (Pokemon pokemon : pokemons) {
            names.add(pokemon.name);
        }
        return names;
    }
}