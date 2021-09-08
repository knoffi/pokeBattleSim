package com.example.demo.Combat.Logs;

import java.util.Optional;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.PhraseStore;
import com.example.demo.Translater.Translater;

public class AttackLog extends CombatLog {

    public AttackLog(boolean blueActs, String attacker, String move, Languages language, Optional<String> type) {
        this.blueActs = blueActs;
        this.attackPokeType = type.isPresent() ? type.get() : "statusChange";
        this.message = this.createMessage(attacker, move, language);
        this.type = CombatLogType.ATTACK.type;
    }

    private String createMessage(String attacker, String move, Languages language) {
        // TODO: get translated name from Pokemon (myPokemon.nameMap.get(language))
        String translatedAttacker = Translater.getTranslatedName(attacker, language);
        String attackerForPhrase = this.isEuropean(language) ? translatedAttacker.toUpperCase() : translatedAttacker;
        // TODO: get translated name from Move (myMove.nameMap.get(language))
        String translatedMove = Translater.getTranslatedAttack(move, language);
        String moveForPhrase = this.isEuropean(language) ? translatedMove.toUpperCase() : translatedMove;

        String message = PhraseStore.getAttackPhrase(language).replace("XXX", attackerForPhrase).replace("YYY",
                moveForPhrase);

        return message;
    }

}