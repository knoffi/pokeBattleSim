package com.example.demo.Controller;

import java.io.IOException;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Pokemon.Pokemon;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private TeamFactory team;
    @Autowired
    private LogTeamConverter teamText;

    @Autowired
    private DuellFactory duell;

    public DuellLog get(Languages lng) {
        try {
            Pokemon[] blue = this.team.get(lng);
            Pokemon[] red = this.team.get(lng);
            LogPokemon[] blueLogPokemon = this.teamText.get(blue);
            LogPokemon[] redLogPokemon = this.teamText.get(red);
            LogRound[] rounds = this.duell.get(red, blue, lng);
            boolean blueWon = this.blueWon(rounds);
            return new DuellLog(blueLogPokemon, redLogPokemon, rounds, blueWon);
        } catch (RuntimeException | IOException | InterruptedException e) {
            System.out.println(e);
            return new DuellLog();
        }
    }

    private boolean blueWon(LogRound[] rounds) {
        return rounds[rounds.length - 1].blueWon;
    }
}