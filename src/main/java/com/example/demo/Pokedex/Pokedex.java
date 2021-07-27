package com.example.demo.Pokedex;

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

import com.example.demo.JSONHandler;
import com.example.demo.RequestMode;
import com.example.demo.Searches.PokemonSearch.PokemonSearch;
import com.example.demo.Searches.PokemonsSearch.PokemonBySearch;
import com.example.demo.Searches.PokemonsSearch.PokemonsSearch;

public class Pokedex {
    private static int CLASSICAL_POKEMON_RANGE = 151;
    private static String API_HOST = "https://pokeapi.co/api/";
    private static String POKEMON_PATH = "v2/pokemon/";;
    private static String CLASSICAL_POKEMON_QUERY = "?limit=" + CLASSICAL_POKEMON_RANGE + "/";

    public static List<String> getClassicalPokemons(RequestMode mode) throws IOException, InterruptedException {
        PokemonBySearch[] pokemons = mode == RequestMode.JAVA_11 ? getClassicalPokemonModernly()
                : getClassicalPokemonNotModernly();
        return getNames(pokemons);
    }

    private static PokemonBySearch[] getClassicalPokemonModernly() throws IOException, InterruptedException {
        return getPokeDataModernly(POKEMON_PATH + CLASSICAL_POKEMON_QUERY, PokemonsSearch.class).results;

    }

    public static <T> T getPokeData(String dataPath, Class<T> type, RequestMode mode)
            throws IOException, InterruptedException, RuntimeException {
        return mode == RequestMode.TRADITIONAL_OLD ? getPokeDataNotModernly(dataPath, type)
                : getPokeDataModernly(dataPath, type);
    }

    private static <T> T getPokeDataModernly(String dataPath, Class<T> type) throws IOException, InterruptedException {
        var url = URI.create(API_HOST + dataPath);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(url).build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        T data = JSONHandler.convertJSON(responseBody, type);
        return data;
    }

    private static <T> T getPokeDataNotModernly(String dataPath, Class<T> type) throws IOException, RuntimeException {
        URL pokemonsURL = new URL(API_HOST + dataPath);
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
        T data = JSONHandler.convertJSON(responseString, type);
        scanner.close();
        connection.disconnect();
        return data;
    }

    private static PokemonBySearch[] getClassicalPokemonNotModernly() throws RuntimeException, IOException {
        return getPokeDataNotModernly(POKEMON_PATH + CLASSICAL_POKEMON_QUERY, PokemonsSearch.class).results;
    }

    private static ArrayList<String> getNames(PokemonBySearch[] pokemons) {
        ArrayList<String> names = new ArrayList<String>();
        for (PokemonBySearch pokemon : pokemons) {
            names.add(pokemon.name);
        }
        return names;
    }

    public static String getStringPokeData(int index) throws IOException, InterruptedException {
        var url = URI.create(API_HOST + POKEMON_PATH + index);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(url).build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        return responseBody;
    }

    public static PokemonSearch getPokemon(int index, RequestMode mode)
            throws RuntimeException, IOException, InterruptedException {
        if (index <= 0 || index > CLASSICAL_POKEMON_RANGE) {
            throw new RuntimeException("We only deal with the Pokemon from the red/blue edition");
        }
        String pathByPokeIndex = POKEMON_PATH + index + "/";
        PokemonSearch pokemon = getPokeData(pathByPokeIndex, PokemonSearch.class, mode);
        return pokemon;
    }

    public static PokemonSearch getPokemon(String name, RequestMode mode)
            throws RuntimeException, IOException, InterruptedException {
        String pathByPokeName = POKEMON_PATH + name + "/";
        return getPokeData(pathByPokeName, PokemonSearch.class, mode);
    }

    public static PokemonSearch[] getRandomTeam(RequestMode mode)
            throws RuntimeException, IOException, InterruptedException {
        PokemonSearch[] pokemonTeam = new PokemonSearch[6];
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