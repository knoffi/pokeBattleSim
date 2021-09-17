package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.PhraseStore;
import com.example.demo.Pokemon.Status.StatusKeys;

public class StatusEffectLog extends CombatLog {
    public StatusEffectLog(boolean blueActs, String target, StatusKeys status, Languages language) {
        // front end needs this field
        this.blueActs = blueActs;
        this.type = CombatLogType.TEXT.type;
        this.message = this.createMessage(target, status, language);
    }

    private String createMessage(String target, StatusKeys status, Languages language) {
        return PhraseStore.getStatusEffectDiffPhrase(language, status).replaceAll("XXX", target);
    }
}
