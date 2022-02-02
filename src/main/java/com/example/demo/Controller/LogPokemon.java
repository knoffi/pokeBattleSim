package com.example.demo.Controller;

import java.util.Arrays;

import com.example.demo.Pokemon.Pokemon;

import org.springframework.stereotype.Component;

public class LogPokemon {
    public String name;
    public String backSprite;
    public String frontSprite;

    public LogPokemon(String name, String backSprite, String frontSprite) {
        this.name = name;
        this.backSprite = backSprite;
        this.frontSprite = frontSprite;
    }
}

@Component
class LogTeamConverter {
    public LogPokemon[] get(Pokemon[] team) {
        return Arrays.stream(team).map(pokemon -> pokemon.getLogData()).toArray(LogPokemon[]::new);
    }
}
