package com.example.demo.Combat.PhraseStore;

import com.example.demo.Translater.Translater;
import com.example.demo.TypeEffects.Effectiveness;

class PhraseRow {
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
        this.veryEffective = Translater.getTranslatedText(Phrases.veryEffect.text, language.key);
        this.superEffective = Translater.getTranslatedText(Phrases.superEffect.text, language.key);
        this.notVeryEffective = Translater.getTranslatedText(Phrases.notVeryEffect.text, language.key);
        this.normalEffective = Phrases.normalEffect.text;
        this.barelyEffective = Translater.getTranslatedText(Phrases.veryBadEffect.text, language.key);
        this.immunEffective = Translater.getTranslatedText(Phrases.immunEffect.text, language.key);
        this.attackText = Translater.getTranslatedText(Phrases.attack.text, language.key);
        this.resultText = Translater.getTranslatedText(Phrases.result.text, language.key);
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