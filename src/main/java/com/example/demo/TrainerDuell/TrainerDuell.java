package com.example.demo.TrainerDuell;

import java.util.Arrays;
import java.util.Stack;

import com.example.demo.Combat.Combat;
import com.example.demo.Combat.CombatResult;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.Trainer.Trainer;

public class TrainerDuell {
    private Stack<Pokemon> redTeam;
    private String redTrainer;
    private String blueTrainer;
    private Stack<Pokemon> blueTeam;
    private Stack<String> duellSummary;

    public TrainerDuell(Trainer red, Trainer blue) {
        this.redTeam = new Stack<Pokemon>();
        for (Pokemon pokemon : red.team) {
            this.redTeam.push(pokemon);
        }
        this.blueTeam = new Stack<Pokemon>();
        for (Pokemon pokemon : blue.team) {
            this.blueTeam.push(pokemon);
        }
        this.blueTrainer = blue.name;
        this.redTrainer = red.name;
        this.duellSummary = new Stack<String>();
    }

    public CombatResult letThemFight() {
        boolean blueCanFight = !this.blueTeam.empty();
        boolean redCanFight = !this.redTeam.empty();
        while (blueCanFight && redCanFight) {
            Pokemon blueFighter = this.blueTeam.pop();
            Pokemon redFighter = this.redTeam.pop();
            CombatResult intermediateResult = new Combat(redFighter, blueFighter).getResult();
            Arrays.stream(intermediateResult.commentary).forEach(comment -> this.duellSummary.push(comment));
            if (intermediateResult.blueWon) {
                this.blueTeam.push(blueFighter);
                redCanFight = !this.redTeam.empty();
            } else {
                this.redTeam.push(redFighter);
                blueCanFight = !this.blueTeam.empty();
            }
        }
        boolean blueWon = blueCanFight;
        String winnerName = blueWon ? blueTrainer : redTrainer;
        String loserName = blueWon ? redTrainer : blueTrainer;
        this.duellSummary.push(winnerName + " was victorious! " + loserName + " fainted...");
        String[] combatSummary = this.duellSummary.toArray(String[]::new);
        return new CombatResult(winnerName, combatSummary, blueCanFight);
    }

}

class IntermediateResult {
    public Pokemon[] redTeam;
    public Pokemon[] blueTeam;
}

class DuellResult {

}
