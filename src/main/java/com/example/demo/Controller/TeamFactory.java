package com.example.demo.Controller;

import java.io.IOException;
import java.util.Arrays;

import com.example.demo.RequestMode;
import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.Searches.PokemonSearch.PokemonSearch;

import org.springframework.stereotype.Component;

@Component
class TeamFactory {
    public Pokemon[] get(Languages lng) throws IOException, InterruptedException {
        PokemonSearch[] pokemonSearches = Pokedex.getRandomTeam(RequestMode.JAVA_11);
        Pokemon[] randomTeam = Arrays.stream(pokemonSearches).map(search -> search.convert(lng))
                .toArray(Pokemon[]::new);
        return randomTeam;
    }
}
