package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.PhraseStore;
import com.example.demo.Pokemon.Status.StatusKeys;

public class StatusLog extends CombatLog {
    public StatusLog(boolean blueActs, String target, StatusKeys status, Languages language) {
        // front end needs this field
        this.blueActs = blueActs;
        this.type = CombatLogType.TEXT.type;
        this.message = this.createMessage(target, status, language);
    }

    private String createMessage(String target, StatusKeys status, Languages language) {

        return PhraseStore.getStatusPhrase(language, status).replaceAll("XXX", target);
    }
}
