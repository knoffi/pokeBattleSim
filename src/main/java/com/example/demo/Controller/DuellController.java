package com.example.demo.Controller;

import java.io.IOException;
import java.util.Arrays;

import com.example.demo.RequestMode;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.Searches.PokemonSearch.PokemonSearch;
import com.example.demo.TrainerDuell.TrainerDuell;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DuellController {
    @GetMapping(value = "/getTrainerDuell")
    public @ResponseBody DuellLog index(@RequestParam(required = false) String lng) {
        try {
            Pokemon[] blue = this.getRandomPokemonTeam();
            Pokemon[] red = this.getRandomPokemonTeam();
            this.translatePokemons(blue, lng);
            this.translatePokemons(red, lng);
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
        }
        return new DuellLog();
    }

    private Pokemon[] getRandomPokemonTeam() throws IOException, InterruptedException {
        PokemonSearch[] pokemonSearches = Pokedex.getRandomTeam(RequestMode.JAVA_11);
        Pokemon[] randomTeam = Arrays.stream(pokemonSearches).map(search -> search.convert()).toArray(Pokemon[]::new);
        return randomTeam;

    }

    private void translatePokemons(Pokemon[] pokemons, String translationRequestParam) {
        Arrays.stream(pokemons).forEach(pokemon -> pokemon.translateName(translationRequestParam));
    }

}
