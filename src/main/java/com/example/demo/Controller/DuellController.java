package com.example.demo.Controller;

import java.io.IOException;

import com.example.demo.RequestMode;
import com.example.demo.Pokedex.Pokedex;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DuellController {
    @GetMapping(value = "/getTrainerDuell")
    public @ResponseBody DuellLog index() {
        String[] firstRoundText = { "Looks like we are in a Pokemon battle, M-M-Morty", "Oh geez, Rick!" };
        LogRound firstRound = new LogRound("ekans", "magnemite", firstRoundText, true);
        LogRound[] rounds = { firstRound };
        LogPokemon bluePokemon;
        LogPokemon redPokemon;
        try {
            bluePokemon = Pokedex.getPokemon(10, RequestMode.JAVA_11).convert().getLogData();
            redPokemon = Pokedex.getPokemon(15, RequestMode.JAVA_11).convert().getLogData();
        } catch (RuntimeException | IOException | InterruptedException e) {
            System.out.println(e);
            bluePokemon = new LogPokemon("magnemite",
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/81.png",
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/81.png");
            redPokemon = new LogPokemon("magnemite",
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/81.png",
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/81.png");
        }
        LogPokemon[] blue = { bluePokemon, bluePokemon };
        LogPokemon[] red = { redPokemon };
        return new DuellLog(blue, red, rounds);
    }
}
