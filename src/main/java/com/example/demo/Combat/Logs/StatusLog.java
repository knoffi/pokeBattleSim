package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;

public class StatusLog extends CombatLog {
    public StatusLog(boolean blueActs, String target, String status, Languages language) {
        // front end needs this field
        this.blueActs = blueActs;
        this.type = CombatLogType.TEXT.type;
        this.message = this.createMessage(target, status, language);
    }

    private String createMessage(String target, String status, Languages language) {

        return target.toUpperCase() + " status changed: " + status;
    }
}
