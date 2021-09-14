package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.PhraseStore;

public class SpeedContestLog extends CombatLog {
    public SpeedContestLog(boolean blueActs, String winner, int stepsAhead, Languages language) {
        // front end needs this field
        this.blueActs = blueActs;
        this.type = CombatLogType.TEXT.type;
        this.message = this.createMessage(winner, stepsAhead, language);
    }

    private String createMessage(String winner, int stepsAhead, Languages language) {

        String phrase = PhraseStore.getSpeedDiffPhrase(language, stepsAhead);

        String winnerForPhrase = this.isEuropean(language) ? winner.toUpperCase() : winner;

        String message = phrase.replaceAll("XXX", winnerForPhrase);
        return message;
    }
}