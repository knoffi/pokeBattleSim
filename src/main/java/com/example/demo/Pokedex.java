package com.example.demo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Pokedex {
    private static int CLASSICAL_POKEMON_RANGE = 151;

    public static List<String> getClassicalPokemons() {
        List<String> names = new ArrayList<String>();
        try {
            URL squirtelURL = new URL("https://pokeapi.co/api/v2/pokemon/?limit=" + CLASSICAL_POKEMON_RANGE + "/");
            HttpURLConnection connection = (HttpURLConnection) squirtelURL.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                String responseString = "";
                Scanner scanner = new Scanner(squirtelURL.openStream());
                while (scanner.hasNext()) {
                    responseString += scanner.nextLine();
                }
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                Pokemon[] pokemons = mapper.readValue(responseString, SearchAnswer.class).results;
                for (Pokemon pokemon : pokemons) {
                    names.add(pokemon.name);
                }
                scanner.close();
                connection.disconnect();
            } else {
                throw new RuntimeException("Request failed: Http Error " + responseCode);
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        return names;
    }
}