package com.example.demo.Controller;

public class DuellLog {
    public LogPokemon[] blueTeam;
    public LogPokemon[] redTeam;
    public LogRound[] rounds;

    public DuellLog(LogPokemon[] blue, LogPokemon[] red, LogRound[] rounds) {
        this.blueTeam = blue;
        this.redTeam = red;
        this.rounds = rounds;
    }

    public DuellLog() {
        LogPokemon[] blue = {};
        LogPokemon[] red = {};
        LogRound[] rounds = {};
        this.blueTeam = blue;
        this.redTeam = red;
        this.rounds = rounds;
    }
}