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
        boolean blueWins = new BattleCalculation(this.blue, this.red).blueWins();
        Pokemon winner = blueWins ? this.blue : this.red;
        Pokemon loser = blueWins ? this.red : this.blue;

        winner.addExhaustion();

        this.combatSummary.push(loser.getName() + " attacks with " + loser.getFinishingBlow());
        this.combatSummary.push(winner.getName() + " hits back with " + winner.getFinishingBlow());
        this.combatSummary.push(winner.getName() + " defeated " + loser.getName() + " !");

        String[] combatSummary = this.combatSummary.toArray(String[]::new);
        return new LogRound(this.red.getName(), this.blue.getName(), combatSummary, blueWins);
    }

}

class BattleCalculation {
    private Pokemon blue;
    private Pokemon red;

    public BattleCalculation(Pokemon blue, Pokemon red) {
        this.blue = blue;
        this.red = red;
    }

    public boolean blueWins() {
        int blueVictoryPoints = 0;
        blueVictoryPoints += this.blueStatBonus();
        blueVictoryPoints += this.blueExhaustionBonus();
        if (blueVictoryPoints > 0) {
            return true;
        } else if (blueVictoryPoints < 0) {
            return false;
        } else {
            return this.blueWinsRandomly();
        }
    }

    private int blueExhaustionBonus() {
        int blueExhaustioDiff = this.blue.getExhaustion() - this.red.getExhaustion();
        boolean blueIsExhausted = blueExhaustioDiff > 0;
        if (blueIsExhausted) {
            return -(int) Math.pow(2, blueExhaustioDiff);
        } else {
            boolean redIsExhausted = blueExhaustioDiff < 0;
            if (redIsExhausted) {
                return (int) Math.pow(2, -blueExhaustioDiff);
            }
        }
        return 0;
    }

    private boolean blueWinsRandomly() {
        boolean blueWins = Math.random() < 0.5;
        return blueWins;
    }

    private int blueStatBonus() {
        return this.blueSumDiff();
    }

    private int blueSumDiff() {
        int sumRed = this.red.getStatSum();
        int sumBlue = this.blue.getStatSum();
        return (int) Math.round((sumBlue - sumRed) / 15);
    }
}
