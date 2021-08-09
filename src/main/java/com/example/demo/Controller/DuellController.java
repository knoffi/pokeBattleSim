package com.example.demo.Controller;

import java.io.IOException;
import java.util.Arrays;

import com.example.demo.RequestMode;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.TrainerDuell.TrainerDuell;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DuellController {
    @GetMapping(value = "/getTrainerDuell")
    public @ResponseBody DuellLog index() {
        try {
            Pokemon bluePokemon = Pokedex.getPokemon(10, RequestMode.JAVA_11).convert();
            Pokemon redPokemon = Pokedex.getPokemon(15, RequestMode.JAVA_11).convert();
            Pokemon[] blue = { bluePokemon, bluePokemon };
            Pokemon[] red = { redPokemon };
            LogPokemon[] blueLogPokemon = Arrays.stream(blue).map(pokemon -> pokemon.getLogData())
                    .toArray(LogPokemon[]::new);
            LogPokemon[] redLogPokemon = Arrays.stream(red).map(pokemon -> pokemon.getLogData())
                    .toArray(LogPokemon[]::new);
            TrainerDuell duell = new TrainerDuell(red, blue);
            LogRound[] rounds = duell.letThemFight();
            return new DuellLog(blueLogPokemon, redLogPokemon, rounds);
        } catch (RuntimeException | IOException | InterruptedException e) {
            System.out.println(e);
        }
        return new DuellLog();
    }
}
