package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.Phrases;
import com.example.demo.Pokemon.Status.StatusKeys;

public class StatusLog extends CombatLog {
    public StatusLog(boolean blueActs, String target, StatusKeys status, Languages language) {
        // front end needs this field
        this.blueActs = blueActs;
        this.type = CombatLogType.TEXT.type;
        this.message = this.createMessage(target, status, language);
    }

    private String createMessage(String target, StatusKeys status, Languages language) {

        switch (status) {
            case FREEZE:

                return Phrases.frozen.text.replaceAll("XXX", target);
            case BURN:

                return Phrases.burn.text.replaceAll("XXX", target);
            case SLEEP:

                return Phrases.sleep.text.replaceAll("XXX", target);
            case POISON:

                return Phrases.poison.text.replaceAll("XXX", target);
            case CONFUS:

                return Phrases.confusion.text.replaceAll("XXX", target);
            case PARA:

                return Phrases.paralyzed.text.replaceAll("XXX", target);

            default:
                try {
                    throw new Exception("StatusKeyNotFound");
                } catch (Exception e) {
                    System.out.println("___STATUS KEY NOT FOUND FOR STATUS RESULT!___");
                }
                return Phrases.frozen.text.replaceAll("XXX", target);
        }
    }
}
