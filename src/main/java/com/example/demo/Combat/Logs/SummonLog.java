package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.Phrases;
import com.example.demo.Translater.Translater;

public class SummonLog extends CombatLog {
    public SummonLog(boolean blueActs, String pokemonName, Languages language) {
        this.blueActs = blueActs;
        this.type = CombatLogType.SUMMON.type;
        this.message = this.createMessage(pokemonName, language);
    }

    private String createMessage(String pokemonName, Languages language) {
        String actor = this.blueActs ? "blue" : "red";
        // TODO: get translated name from Pokemon (myPokemon.nameMap.get(language))
        String translatedActor = Translater.getTranslatedText(actor, language.key);
        String actorForPhrase = this.isEuropean(language) ? translatedActor.toUpperCase() : translatedActor;
        // TODO: get translated name from Pokemon (myPokemon.nameMap.get(language))
        String translatedPokemon = Translater.getTranslatedName(pokemonName, language);
        String pokemonForPhrase = this.isEuropean(language) ? translatedPokemon.toUpperCase() : translatedPokemon;

        String phrase = Phrases.summon.text;
        String translatedPhrase = Translater.getTranslatedText(phrase, language.key);
        // TODO: use phrases from PhraseTable to avoid overkill of Google API calls and
        // for better German translation ("Die INITIATIVE von Aerodactyl is gestiegen"
        // <-> "Aerodactyls INITIATIVE ist gestiegen!")
        String message = translatedPhrase.replaceAll("XXX", actorForPhrase).replaceAll("YYY", pokemonForPhrase);
        return message;
    }
}