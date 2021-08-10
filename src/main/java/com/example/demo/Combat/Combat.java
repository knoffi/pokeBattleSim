package com.example.demo.Combat;

import java.util.Stack;

import com.example.demo.Controller.LogRound;
import com.example.demo.Pokemon.Pokemon;

public class Combat {
    private Pokemon red;
    private Pokemon blue;
    private Stack<String> combatSummary;

    public Combat(Pokemon pokemonRed, Pokemon pokemonBlue) {
        this.red = pokemonRed;
        this.blue = pokemonBlue;
        this.combatSummary = new Stack<String>();
    }

    public LogRound getResult() {
        boolean blueWins = Math.random() < 0.5;
        Pokemon winner = blueWins ? this.blue : this.red;
        Pokemon loser = blueWins ? this.red : this.blue;

        this.combatSummary.push(loser.getName() + " attacks with " + loser.getFinishingBlow());
        this.combatSummary.push(winner.getName() + " hits back with " + winner.getFinishingBlow());
        this.combatSummary.push(winner.getName() + " defeated " + loser.getName() + " !");

        String[] combatSummary = this.combatSummary.toArray(String[]::new);
        return new LogRound(this.red.getName(), this.blue.getName(), combatSummary, blueWins);
    }

}
