package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.PhraseStore;
import com.example.demo.Translater.Translater;

public class ResultLog extends CombatLog {
    public ResultLog(boolean blueWins, String name, Languages language) {
        // front end needs this field
        this.blueActs = blueWins;
        this.type = CombatLogType.FAINT.type;
        this.message = this.createMessage(name, language);
    }

    private String createMessage(String name, Languages language) {
        // TODO: get translated name from Pokemon (myPokemon.nameMap.get(language))
        String translatedName = Translater.getTranslatedName(name, language);
        String nameForPhrase = this.isEuropean(language) ? translatedName.toUpperCase() : translatedName;

        return PhraseStore.getResultPhrase(language).replace("XXX", nameForPhrase);
    }
}