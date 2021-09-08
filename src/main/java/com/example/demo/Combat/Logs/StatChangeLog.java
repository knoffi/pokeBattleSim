package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.Phrases;
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

        String phrase = isRising ? Phrases.statRise.text : Phrases.statFall.text;
        String translatedPhrase = Translater.getTranslatedText(phrase, language.key);
        // TODO: use PhraseStore to avoid overkill of Google API calls and
        // for better German translation ("Die INITIATIVE von Aerodactyl is gestiegen"
        // <-> "Aerodactyls INITIATIVE ist gestiegen!")
        String message = translatedPhrase.replaceAll("XXX", targetForPhrase).replaceAll("YYY", statForPhrase);

        return Translater.getTranslatedText(message, language.key);
    }
}