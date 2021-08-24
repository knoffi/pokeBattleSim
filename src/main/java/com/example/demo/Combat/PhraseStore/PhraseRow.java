package com.example.demo.Combat.PhraseStore;

import com.example.demo.Translater.Translater;
import com.example.demo.TypeEffects.Effectiveness;

class PhraseRow {
    final static public String defaultAttackText = "XXX uses YYY.";
    final static public String defaultResultText = "XXX was defeated!";
    final static public String defaultEffectText = "";

    private String languageKey;
    private String veryEffective;
    private String superEffective;
    private String immunEffective;
    private String barelyEffective;
    private String notVeryEffective;
    private String normalEffective;
    private String attackText;
    private String resultText;

    public PhraseRow(Languages language) {
        this.languageKey = language.key;
        this.veryEffective = Translater.getTranslatedText("It is very effective!", language.key);
        this.superEffective = Translater.getTranslatedText("It is super effective!", language.key);
        this.notVeryEffective = Translater.getTranslatedText("It is not very effective!", language.key);
        this.normalEffective = defaultEffectText;
        this.barelyEffective = Translater.getTranslatedText("It is nearly ineffective!", language.key);
        this.immunEffective = Translater.getTranslatedText("Nothing happens!", language.key);
        this.attackText = Translater.getTranslatedText(defaultAttackText, language.key);
        this.resultText = Translater.getTranslatedText(defaultResultText, language.key);
    }

    PhraseRow() {

    }

    public boolean belongsToLanguage(Languages language) {
        return this.languageKey.equals(language.key);
    }

    public String getAttackText() {
        return this.attackText;
    }

    public String getResultText() {
        return this.resultText;
    }

    public String getEffectText(Effectiveness effect) {
        switch (effect) {
            case IMMUN:
                return this.immunEffective;
            case VERY:
                return this.veryEffective;
            case SUPER:
                return this.superEffective;
            case SUPER_BAD:
                return this.barelyEffective;
            case RESISTANT:
                return this.notVeryEffective;

            default:
                return this.normalEffective;
        }
    }

}