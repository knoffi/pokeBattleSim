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

        String attackerForPhrase = this.isEuropean(language) ? attacker.toUpperCase() : attacker;
        // TODO: get move translation from jsonbin by store butler (translating every
        // move at instantiation is not effectiv since half of the created moves are not
        // used. And translating every move just for the log is slightly better, because
        // the same move does only appear twice in text (in average).)
        String translatedMove = Translater.getTranslatedAttack(move, language);
        String moveForPhrase = this.isEuropean(language) ? translatedMove.toUpperCase() : translatedMove;

        String message = PhraseStore.getAttackPhrase(language).replace("XXX", attackerForPhrase).replace("YYY",
                moveForPhrase);

        return message;
    }

}