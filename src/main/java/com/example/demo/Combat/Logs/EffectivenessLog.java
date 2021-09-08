package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.PhraseStore;
import com.example.demo.TypeEffects.Effectiveness;

public class EffectivenessLog extends CombatLog {
    public EffectivenessLog(boolean blueActs, Effectiveness effect, Languages language) {
        this.blueActs = blueActs;
        this.message = this.createMessage(effect, language);
        this.type = CombatLogType.TEXT.type;
    }

    private String createMessage(Effectiveness effect, Languages language) {
        String effectString = PhraseStore.getEffectPhrase(effect, language);

        return effectString;
    }
}