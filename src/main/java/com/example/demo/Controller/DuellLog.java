package com.example.demo.Controller;

import java.io.IOException;
import java.util.Arrays;

import com.example.demo.RequestMode;
import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.Searches.PokemonSearch.PokemonSearch;
import com.example.demo.TrainerDuell.TrainerDuell;

import org.springframework.stereotype.Component;

class DuellLog {
    public LogPokemon[] blueTeam;
    public LogPokemon[] redTeam;
    public LogRound[] rounds;
    public boolean blueWon;

    public DuellLog(LogPokemon[] blue, LogPokemon[] red, LogRound[] rounds, boolean blueWon) {
        this.blueTeam = blue;
        this.redTeam = red;
        this.rounds = rounds;
        this.blueWon = blueWon;
    }

    public DuellLog() {
        LogPokemon[] blue = {};
        LogPokemon[] red = {};
        LogRound[] rounds = {};
        this.blueTeam = blue;
        this.redTeam = red;
        this.rounds = rounds;
        this.blueWon = true;
    }

}

@Component
class DuellLogFactory {
    public DuellLog get(Languages lng) {
        try {
            Pokemon[] blue = this.getRandomPokemonTeam(lng);
            Pokemon[] red = this.getRandomPokemonTeam(lng);
            LogPokemon[] blueLogPokemon = Arrays.stream(blue).map(pokemon -> pokemon.getLogData())
                    .toArray(LogPokemon[]::new);
            LogPokemon[] redLogPokemon = Arrays.stream(red).map(pokemon -> pokemon.getLogData())
                    .toArray(LogPokemon[]::new);
            TrainerDuell duell = new TrainerDuell(red, blue);
            LogRound[] rounds = duell.letThemFight(lng);
            boolean blueWon = rounds[rounds.length - 1].blueWon;
            return new DuellLog(blueLogPokemon, redLogPokemon, rounds, blueWon);
        } catch (RuntimeException | IOException | InterruptedException e) {
            System.out.println(e);
            return new DuellLog();
        }
    }

    private Pokemon[] getRandomPokemonTeam(Languages language) throws IOException, InterruptedException {
        PokemonSearch[] pokemonSearches = Pokedex.getRandomTeam(RequestMode.JAVA_11);
        Pokemon[] randomTeam = Arrays.stream(pokemonSearches).map(search -> search.convert(language))
                .toArray(Pokemon[]::new);
        return randomTeam;

    }
}