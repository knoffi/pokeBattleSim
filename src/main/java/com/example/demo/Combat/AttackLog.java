package com.example.demo.Combat;

import java.util.Optional;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.PhraseStore;

public class AttackLog extends CombatLog {

    public AttackLog(boolean blueActs, String attacker, String move, Languages language, Optional<String> type) {
        this.blueActs = blueActs;
        this.attackPokeType = type.isPresent() ? type.get() : "statusChange";
        this.message = this.createMessage(attacker, move, language);
        this.type = CombatLogType.ATTACK.type;
    }

    private String createMessage(String attacker, String move, Languages language) {
        String message = PhraseStore.getAttackPhrase(language).replace("XXX", attacker).replace("YYY", move);

        return message;
    }

}