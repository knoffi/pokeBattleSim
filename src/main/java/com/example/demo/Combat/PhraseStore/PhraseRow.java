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
    private String blueSummonText;
    private String redSummonText;
    private String statRiseText;
    private String statFallText;
    private String speedDiff0Text;
    private String speedDiff1Text;
    private String speedDiff2Text;
    private String speedDiff3Text;
    private String speedDiff4Text;

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
        this.blueSummonText = Translater.getTranslatedText(Phrases.blueSummon.text, language.key);
        this.redSummonText = Translater.getTranslatedText(Phrases.redSummon.text, language.key);
        this.statFallText = Translater.getTranslatedText(Phrases.statFall.text, language.key);
        this.statRiseText = Translater.getTranslatedText(Phrases.statRise.text, language.key);
        this.speedDiff0Text = Translater.getTranslatedText(Phrases.speedDiff0.text, language.key);
        this.speedDiff1Text = Translater.getTranslatedText(Phrases.speedDiff1.text, language.key);
        this.speedDiff2Text = Translater.getTranslatedText(Phrases.speedDiff2.text, language.key);
        this.speedDiff3Text = Translater.getTranslatedText(Phrases.speedDiff3.text, language.key);
        this.speedDiff4Text = Translater.getTranslatedText(Phrases.speedDiff4.text, language.key);
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

    public String getSpeedDiffText(int speedDiff) {
        switch (speedDiff) {
            case 0:
                return this.speedDiff0Text;
            case 1:
                return this.speedDiff1Text;
            case 2:
                return this.speedDiff2Text;
            case 3:
                return this.speedDiff3Text;

            default:
                return this.speedDiff4Text;
        }
    }

    public String getSummonText(boolean isForBlue) {
        return isForBlue ? this.blueSummonText : this.redSummonText;
    }

    public String getStatText(boolean isRising) {
        return isRising ? this.statRiseText : statFallText;
    }

}