package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.Phrases;
import com.example.demo.Pokemon.Status.StatusKeys;

public class StatusEffectLog extends CombatLog {
    public StatusEffectLog(boolean blueActs, String target, StatusKeys status, Languages language) {
        // front end needs this field
        this.blueActs = blueActs;
        this.type = CombatLogType.TEXT.type;
        this.message = this.createMessage(target, status, language);
    }

    private String createMessage(String target, StatusKeys status, Languages language) {
        switch (status) {
            case FREEZE:

                return Phrases.frozenResult.text;
            case BURN:

                return Phrases.burnResult.text;
            case SLEEP:

                return Phrases.sleepResult.text;
            case POISON:

                return Phrases.poisonResult.text;
            case CONFUS:

                return Phrases.confusionResult.text;
            case PARA:

                return Phrases.paralyzedResult.text;

            default:
                try {
                    throw new Exception("StatusKeyNotFound");
                } catch (Exception e) {
                    System.out.println("___STATUS KEY NOT FOUND FOR STATUS RESULT!___");
                }
                return Phrases.frozenResult.text;
        }
    }
}
