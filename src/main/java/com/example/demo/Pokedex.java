package com.example.demo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Pokedex {
    private static int CLASSICAL_POKEMON_RANGE = 151;
    private static String POKEMON_URL_PATH = "https://pokeapi.co/api/v2/pokemon/";
    private static String CLASSICAL_POKEMON_QUERY = "?limit=" + CLASSICAL_POKEMON_RANGE + "/";
    private static String CLASSICAL_POKEMON_URL = POKEMON_URL_PATH + CLASSICAL_POKEMON_QUERY;

    public static List<String> getClassicalPokemons(HTTPMode mode) throws IOException, InterruptedException {
        Pokemon[] pokemons = mode == HTTPMode.JAVA_11 ? getClassicalPokemonModernly() : getClassicalPokemonRobustly();
        return getNames(pokemons);
    }

    private static Pokemon[] getClassicalPokemonModernly() throws IOException, InterruptedException {
        var pokemonURL = URI.create(CLASSICAL_POKEMON_URL);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(pokemonURL).build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        Pokemon[] pokemons = convertJSON(responseBody);

        return pokemons;

    }

    private static Pokemon[] getClassicalPokemonRobustly() throws RuntimeException, IOException {
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
        scanner.close();
        connection.disconnect();
        return pokemons;

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

    public static Pokemon getPokemon(int index, HTTPMode mode)
            throws RuntimeException, IOException, InterruptedException {
        if (index <= 0 || index > CLASSICAL_POKEMON_RANGE) {
            throw new RuntimeException("We only deal with the Pokemon from the red/blue edition");
        }
        Pokemon pokemon = mode == HTTPMode.JAVA_11 ? getClassicalPokemonModernly()[index]
                : getClassicalPokemonRobustly()[index];
        return pokemon;
    }

    public static Pokemon getPokemon(String name, HTTPMode mode)
            throws RuntimeException, IOException, InterruptedException {
        Pokemon[] pokemons = mode == HTTPMode.JAVA_11 ? getClassicalPokemonModernly() : getClassicalPokemonRobustly();
        Pokemon result = Arrays.stream(pokemons).filter(pokemon -> pokemon.name.equals(name)).findFirst()
                .orElseThrow(RuntimeException::new);
        return result;
    }

    public static Pokemon[] getRandomTeam(HTTPMode mode) throws RuntimeException, IOException, InterruptedException {
        Pokemon[] pokemonTeam = new Pokemon[6];
        for (int i = 0; i < 6; i++) {
            pokemonTeam[i] = getPokemon(getRandomPokeIndex(), mode);
        }

        return pokemonTeam;
    }

    private static int getRandomPokeIndex() {
        int index = (int) Math.round(Math.random() * CLASSICAL_POKEMON_RANGE);
        return index;
    }
}