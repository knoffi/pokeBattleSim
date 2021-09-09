package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.PhraseStore;
import com.example.demo.Translater.Translater;

public class StatChangeLog extends CombatLog {
    public StatChangeLog(boolean blueActs, String target, String stat, boolean isRising, Languages language) {
        // front end needs this field
        this.blueActs = blueActs;
        this.type = CombatLogType.TEXT.type;
        this.message = this.createMessage(target, stat, isRising, language);
    }

    private String createMessage(String target, String stat, boolean isRising, Languages language) {
        // TODO: use PhraseStore to store Stat Translations
        String translatedStat = Translater.getTranslatedStat(stat, language);
        String statForPhrase = this.isEuropean(language) ? translatedStat.toUpperCase() : translatedStat;

        // TODO: get translated name from Pokemon (myPokemon.nameMap.get(language))
        String translatedTarget = Translater.getTranslatedName(target, language);
        String targetForPhrase = this.isEuropean(language) ? translatedTarget.toUpperCase() : translatedTarget;

        String phrase = PhraseStore.getStatPhrase(language, isRising);
        String message = phrase.replaceAll("XXX", targetForPhrase).replaceAll("YYY", statForPhrase);

        return Translater.getTranslatedText(message, language.key);
    }
}