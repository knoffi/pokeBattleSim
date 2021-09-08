package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.Phrases;
import com.example.demo.Translater.Translater;

public class SpeedContestLog extends CombatLog {
    public SpeedContestLog(boolean blueActs, String winner, int stepsAhead, Languages language) {
        // front end needs this field
        this.blueActs = blueActs;
        this.type = CombatLogType.TEXT.type;
        this.message = this.createMessage(winner, stepsAhead, language);
    }

    private String createMessage(String winner, int stepsAhead, Languages language) {

        String phrase;

        switch (stepsAhead) {
            case 0:
                return Translater.getTranslatedText(Phrases.speedDiff0.text, language.key);
            case 1:
                phrase = Phrases.speedDiff1.text;
                break;
            case 2:
                phrase = Phrases.speedDiff2.text;
                break;
            case 3:
                phrase = Phrases.speedDiff3.text;
                break;
            default:
                phrase = Phrases.speedDiff4.text;
                break;
        }

        String translatedPhrase = Translater.getTranslatedText(phrase, language.key);

        // TODO: get translated name from Pokemon (myPokemon.nameMap.get(language))
        String translatedWinner = Translater.getTranslatedName(winner, language);
        String winnerForPhrase = this.isEuropean(language) ? translatedWinner.toUpperCase() : translatedWinner;

        String message = translatedPhrase.replaceAll("XXX", winnerForPhrase);
        return message;
    }
}