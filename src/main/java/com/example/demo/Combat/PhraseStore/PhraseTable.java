package com.example.demo.Combat.PhraseStore;

import java.util.Arrays;
import java.util.Optional;

import com.example.demo.TypeEffects.Effectiveness;

public class PhraseTable {
    private PhraseRow[] rows;

    public PhraseTable() {

    }

    public void updateTranslations() {
        this.rows = Arrays.stream(Languages.values()).map(language -> new PhraseRow(language))
                .toArray(PhraseRow[]::new);
    }

    public String getAttackPhrase(Languages language) {
        Optional<String> attackText = Arrays.stream(this.rows).filter(row -> row.belongsToLanguage(language))
                .map(row -> row.getAttackText()).findAny();

        if (attackText.isPresent()) {
            return attackText.get();
        } else {
            this.throwRowNotFound();
            return Phrases.attack.text;
        }
    }

    public String getResultPhrase(Languages language) {
        Optional<String> resultText = Arrays.stream(this.rows).filter(row -> row.belongsToLanguage(language))
                .map(row -> row.getResultText()).findAny();

        if (resultText.isPresent()) {
            return resultText.get();
        } else {
            this.throwRowNotFound();
            return Phrases.result.text;
        }
    }

    public String getEffectPhrase(Languages language, Effectiveness effect) {
        Optional<String> effectText = Arrays.stream(this.rows).filter(row -> row.belongsToLanguage(language))
                .map(row -> row.getEffectText(effect)).findAny();

        if (effectText.isPresent()) {
            return effectText.get();
        } else {
            this.throwRowNotFound();
            return Phrases.normalEffect.text;
        }
    }

    public String getSummonPhrase(Languages language, boolean isForBlue) {
        Optional<String> effectText = Arrays.stream(this.rows).filter(row -> row.belongsToLanguage(language))
                .map(row -> row.getSummonText(isForBlue)).findAny();

        if (effectText.isPresent()) {
            return effectText.get();
        } else {
            this.throwRowNotFound();
            return Phrases.normalEffect.text;
        }
    }

    public String getSpeedDiffPhrase(Languages language, int speedDiff) {
        Optional<String> effectText = Arrays.stream(this.rows).filter(row -> row.belongsToLanguage(language))
                .map(row -> row.getSpeedDiffText(speedDiff)).findAny();

        if (effectText.isPresent()) {
            return effectText.get();
        } else {
            this.throwRowNotFound();
            switch (speedDiff) {
                case 0:
                    return Phrases.speedDiff0.text;
                case 1:
                    return Phrases.speedDiff1.text;
                case 2:
                    return Phrases.speedDiff2.text;
                case 3:
                    return Phrases.speedDiff3.text;

                default:
                    return Phrases.speedDiff4.text;
            }

        }
    }

    public String getStatPhrase(Languages language, boolean isRising) {
        Optional<String> effectText = Arrays.stream(this.rows).filter(row -> row.belongsToLanguage(language))
                .map(row -> row.getStatText(isRising)).findAny();

        if (effectText.isPresent()) {
            return effectText.get();
        } else {
            this.throwRowNotFound();
            return isRising ? Phrases.statRise.text : Phrases.statFall.text;
        }
    }

    private void throwRowNotFound() {
        try {
            throw new Exception("LanguageRowNotFound");
        } catch (Exception e) {
            System.out.println("___NO ROW FOR DESIRED LANGUAGE___" + e.getClass());
        }
    }

}