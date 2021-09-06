package com.example.demo.Combat;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.PhraseStore;
import com.example.demo.Translater.Translater;
import com.example.demo.TypeEffects.Effectiveness;

public class CombatText {
    private String blue;
    private String red;
    private String blueAttack;
    private String redAttack;
    private Effectiveness blueEffect;
    private Effectiveness redEffect;
    private boolean blueWon;
    private Languages language;

    public CombatText(String blue, String blueAttack, Effectiveness blueEffect, String red, String redAttack,
            Effectiveness redEffect, Languages language, boolean blueWon) {
        this.language = language;
        this.blue = Translater.getTranslatedName(blue, language);
        this.red = Translater.getTranslatedName(red, language).toUpperCase();
        this.blueAttack = Translater.getTranslatedAttack(blueAttack, language);
        this.redAttack = Translater.getTranslatedAttack(redAttack, language).toUpperCase();
        this.blueWon = blueWon;
        this.blueEffect = blueEffect;
        this.redEffect = redEffect;

        this.adjustNames();
    }

    private void adjustNames() {
        boolean isEuropeanLanguage = this.language.isEuropean();
        if (isEuropeanLanguage) {
            this.red = this.red.toUpperCase();
            this.redAttack = this.redAttack.toUpperCase();
            this.blue = this.blue.toUpperCase();
            this.blueAttack = this.blueAttack.toUpperCase();
        }
    }

    public String getAttackText(boolean blueAttacks) {
        Effectiveness effect = blueAttacks ? this.blueEffect : this.redEffect;
        String attacker = blueAttacks ? this.blue : this.red;
        String move = blueAttacks ? this.blueAttack : this.redAttack;
        String effectString = PhraseStore.getEffectPhrase(effect, language);
        String attackString = PhraseStore.getAttackPhrase(language).replace("XXX", attacker).replace("YYY", move);

        return attackString + " " + effectString;
    }

    public String getResultText() {
        String loser = this.blueWon ? this.red : this.blue;
        String resultString = PhraseStore.getResultPhrase(language).replace("XXX", loser);

        return resultString;
    }

}