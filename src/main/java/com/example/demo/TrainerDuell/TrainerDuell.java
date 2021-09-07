package com.example.demo.TrainerDuell;

import java.util.Stack;

import com.example.demo.Combat.Combat;
import com.example.demo.Combat.VeteranMode;
import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Controller.LogRound;
import com.example.demo.Pokemon.Pokemon;

public class TrainerDuell {
    private Stack<Pokemon> redTeam;
    private Stack<Pokemon> blueTeam;
    private Stack<LogRound> duellSummary;

    public TrainerDuell(Pokemon[] red, Pokemon[] blue) {
        this.redTeam = new Stack<Pokemon>();
        for (Pokemon pokemon : red) {
            this.redTeam.push(pokemon);
        }
        this.blueTeam = new Stack<Pokemon>();
        for (Pokemon pokemon : blue) {
            this.blueTeam.push(pokemon);
        }
        this.duellSummary = new Stack<LogRound>();
    }

    public LogRound[] letThemFight(Languages language) {
        boolean blueCanFight = !this.blueTeam.empty();
        boolean redCanFight = !this.redTeam.empty();
        VeteranMode veteran = VeteranMode.NONE;
        while (blueCanFight && redCanFight) {

            Pokemon blueFighter = this.blueTeam.pop();
            Pokemon redFighter = this.redTeam.pop();

            LogRound roundSummary = Combat.getResult(redFighter, blueFighter, language, veteran);
            this.duellSummary.push(roundSummary);

            if (roundSummary.blueWon) {
                veteran = VeteranMode.BLUE;
                this.blueTeam.push(blueFighter);
                redCanFight = !this.redTeam.empty();
            } else {
                veteran = VeteranMode.RED;
                this.redTeam.push(redFighter);
                blueCanFight = !this.blueTeam.empty();
            }
        }
        return this.duellSummary.toArray(LogRound[]::new);
    }
}

class IntermediateResult {
    public Pokemon[] redTeam;
    public Pokemon[] blueTeam;
}
