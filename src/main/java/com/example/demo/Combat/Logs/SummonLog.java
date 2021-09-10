package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.PhraseStore;

public class SummonLog extends CombatLog {
    public SummonLog(boolean blueActs, String pokemonName, Languages language) {
        this.blueActs = blueActs;
        this.type = CombatLogType.SUMMON.type;
        this.message = this.createMessage(pokemonName, language);
    }

    private String createMessage(String pokemon, Languages language) {
        String pokemonForPhrase = this.isEuropean(language) ? pokemon.toUpperCase() : pokemon;

        String phrase = PhraseStore.getSummonPhrase(language, blueActs);

        String message = phrase.replaceAll("XXX", pokemonForPhrase);
        return message;
    }
}