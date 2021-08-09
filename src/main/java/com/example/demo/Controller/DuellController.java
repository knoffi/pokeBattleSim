package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DuellController {
    @GetMapping(value = "/getTrainerDuell")
    public @ResponseBody DuellLog index() {
        LogPokemon bluePokemon = new LogPokemon("ekans",
                "https ://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/23.png",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/23.png");
        LogPokemon redPokemon = new LogPokemon("magnemite",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/81.png",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/81.png");
        String[] firstRoundText = { "Looks like we are in a Pokemon battle, M-M-Morty", "Oh geez, Rick!" };
        LogRound firstRound = new LogRound("ekans", "magnemite", firstRoundText, true);
        LogRound[] rounds = { firstRound };
        LogPokemon[] blue = { bluePokemon, bluePokemon };
        LogPokemon[] red = { redPokemon };
        return new DuellLog(blue, red, rounds);
    }
}

class DuellLog {
    public LogPokemon[] blueTeam;
    public LogPokemon[] redTeam;
    public LogRound[] rounds;

    DuellLog(LogPokemon[] blue, LogPokemon[] red, LogRound[] rounds) {
        this.blueTeam = blue;
        this.redTeam = red;
        this.rounds = rounds;
    }
}

class LogPokemon {
    public String name;
    public String backSprite;
    public String frontSprite;

    public LogPokemon(String name, String backSprite, String frontSprite) {
        this.name = name;
        this.backSprite = backSprite;
        this.frontSprite = frontSprite;
    }
}

class LogRound {
    public String redCombatant;
    public String blueCombatant;
    public String[] battleLog;
    public boolean blueWon;

    public LogRound(String redCombatant, String blueCombatant, String[] battleLog, boolean blueWon) {
        this.redCombatant = redCombatant;
        this.blueCombatant = blueCombatant;
        this.battleLog = battleLog;
        this.blueWon = blueWon;
    }
}
