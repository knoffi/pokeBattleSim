package com.example.demo.TrainerDuell;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import com.example.demo.Combat.Combat;
import com.example.demo.Controller.LogRound;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.Translater.Translater;

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

    public LogRound[] letThemFight(String languageParam) {
        boolean blueCanFight = !this.blueTeam.empty();
        boolean redCanFight = !this.redTeam.empty();
        while (blueCanFight && redCanFight) {
            Pokemon blueFighter = this.blueTeam.pop();
            Pokemon redFighter = this.redTeam.pop();
            LogRound roundSummary = new Combat(redFighter, blueFighter, languageParam).getResult();
            this.duellSummary.push(roundSummary);
            if (roundSummary.blueWon) {
                this.blueTeam.push(blueFighter);
                redCanFight = !this.redTeam.empty();
            } else {
                this.redTeam.push(redFighter);
                blueCanFight = !this.blueTeam.empty();
            }
        }
        this.translateBattleLogs(languageParam);
        return this.duellSummary.toArray(LogRound[]::new);
    }

    private void translateBattleLogs(String languageParam) {
        List<String> battleTexts = this.duellSummary.stream().flatMap(round -> Arrays.stream(round.battleLog))
                .collect(Collectors.toList());
        var translatedBattleTexts = Translater.getTranslatedTexts(battleTexts, languageParam);
        var textsIterator = translatedBattleTexts.iterator();
        var roundIterator = this.duellSummary.iterator();
        while (textsIterator.hasNext()) {
            String firstText = textsIterator.next();
            String secondText = textsIterator.next();
            String thirdText = textsIterator.next();

            LogRound round = roundIterator.next();

            String[] newBatteLog = { firstText, secondText, thirdText };
            round.battleLog = newBatteLog;

        }
    }

}

class IntermediateResult {
    public Pokemon[] redTeam;
    public Pokemon[] blueTeam;
}
