package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.PhraseStore;
import com.example.demo.Translater.Translater;

public class SummonLog extends CombatLog {
    public SummonLog(boolean blueActs, String pokemonName, Languages language) {
        this.blueActs = blueActs;
        this.type = CombatLogType.SUMMON.type;
        this.message = this.createMessage(pokemonName, language);
    }

    private String createMessage(String pokemonName, Languages language) {
        // TODO: get translated name from Pokemon (myPokemon.nameMap.get(language))
        String translatedPokemon = Translater.getTranslatedName(pokemonName, language);
        String pokemonForPhrase = this.isEuropean(language) ? translatedPokemon.toUpperCase() : translatedPokemon;

        String phrase = PhraseStore.getSummonPhrase(language, blueActs);

        String message = phrase.replaceAll("XXX", pokemonForPhrase);
        return message;
    }
}