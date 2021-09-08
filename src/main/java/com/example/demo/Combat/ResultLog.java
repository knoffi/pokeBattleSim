package com.example.demo.Combat;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.PhraseStore;

class ResultLog extends CombatLog {
    ResultLog(boolean blueWins, String name, Languages language) {
        // front end needs this field
        this.blueActs = blueWins;
        this.type = CombatLogType.FAINT.type;
        this.message = this.createMessage(name, language);
    }

    private String createMessage(String name, Languages language) {
        return PhraseStore.getResultPhrase(language).replace("XXX", name);
    }
}