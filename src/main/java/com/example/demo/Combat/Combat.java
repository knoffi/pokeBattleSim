package com.example.demo.Combat;

import java.util.Stack;

import com.example.demo.Controller.LogRound;
import com.example.demo.Pokemon.Attack;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.Pokemon.Type;
import com.example.demo.Translater.Translater;
import com.example.demo.TypeEffects.Effectiveness;
import com.example.demo.TypeEffects.TypeStore;

public class Combat {
    private Pokemon red;
    private Pokemon blue;
    private Stack<String> combatSummary;
    private String languageParam;

    public Combat(Pokemon pokemonRed, Pokemon pokemonBlue, String languageParam) {
        this.red = pokemonRed;
        this.blue = pokemonBlue;
        this.combatSummary = new Stack<String>();
        this.languageParam = languageParam;
    }

    public LogRound getResult() {
        final CombatResult combatResult = new BattleCalculation(this.blue, this.red, this.languageParam).getResult();
        final boolean blueWins = combatResult.blueWin;
        final Pokemon winner = blueWins ? this.blue : this.red;

        winner.addExhaustion();

        this.combatSummary.addAll(combatResult.texts);

        final String[] combatSummary = this.combatSummary.toArray(String[]::new);
        return new LogRound(this.red.getName(), this.blue.getName(), combatSummary, blueWins);
    }

}

class BattleCalculation {
    /**
     *
     */
    private static final double STAT_WEIGHT = 1.0 / 15;
    /**
     *
     */
    private static final int EXHAUSTION_WEIGHT_BASE = 2;
    /**
     *
     */
    private static final int ATTACK_WEIGHT = 8;
    private Pokemon blue;
    private Pokemon red;
    private AttackNameAndEffect blueAttack;
    private AttackNameAndEffect redAttack;
    private String languageParam;

    public BattleCalculation(Pokemon blue, Pokemon red, String languageParam) {
        this.blue = blue;
        this.red = red;
        this.blueAttack = getBestAttackEffect(this.blue.getFinishingBlows(), this.red.getPokeTypes(), languageParam);
        this.redAttack = getBestAttackEffect(this.red.getFinishingBlows(), this.blue.getPokeTypes(), languageParam);
        this.languageParam = languageParam;
    }

    public CombatResult getResult() {
        double blueVictoryPoints = 0;
        double statBonus = this.blueStatBonus();
        int exhaustBonus = this.blueExhaustionBonus();
        double attackBonus = this.blueAttackBonus();
        // System.out.println("stat bonus :" + statBonus);
        // System.out.println("exhaust bonus :" + exhaustBonus);
        // System.out.println("attack bonus :" + attackBonus);
        blueVictoryPoints += statBonus + exhaustBonus + attackBonus;
        // System.out.println("result :" + blueVictoryPoints);
        // System.out.println("_________________________________");
        boolean blueWins = blueVictoryPoints != 0 ? blueVictoryPoints > 0 : this.blueWinsRandomly();
        final Stack<String> texts = this.getResultTexts(blueWins);
        return new CombatResult(blueWins, texts);
    }

    private double blueAttackBonus() {
        double blueImpact = this.blueAttack.effect.value * ATTACK_WEIGHT;
        double redImpact = this.redAttack.effect.value * ATTACK_WEIGHT;
        if (blueImpact == Effectiveness.IMMUN.value && redImpact == Effectiveness.IMMUN.value) {
            return 0;
        } else {
            return blueImpact - redImpact;
        }

    }

    private Stack<String> getResultTexts(boolean blueWins) {
        final String blueAttack = AttackText.buildString(this.blue.getName(), this.blueAttack);
        final String redAttack = AttackText.buildString(this.red.getName(), this.redAttack);
        final String loserName = blueWins ? this.red.getName() : this.blue.getName();
        final String endResult = loserName + " was defeated!";

        final String translatedBlueAttack = Translater.getTranslatedText(blueAttack, this.languageParam);
        final String translatedRedAttack = Translater.getTranslatedText(redAttack, this.languageParam);
        final String translatedEndResult = Translater.getTranslatedText(endResult, this.languageParam);
        Stack<String> texts = new Stack<String>();
        texts.add(translatedBlueAttack);
        texts.add(translatedRedAttack);
        texts.add(translatedEndResult);
        return texts;

    }

    private static AttackNameAndEffect getBestAttackEffect(Attack[] attacks, Type[] defender, String languageParam) {
        Effectiveness maxEffectiveness = Effectiveness.IMMUN;

        String bestAttackName = "struggle";
        for (Attack attack : attacks) {
            Effectiveness newEffectiveness = getEffectiveness(attack.getType(), defender);
            if (isBiggerOrEqual(newEffectiveness, maxEffectiveness)) {
                bestAttackName = attack.getName();
                maxEffectiveness = newEffectiveness;
            }
        }
        return new AttackNameAndEffect(bestAttackName, maxEffectiveness, languageParam);
    }

    private static boolean isBiggerOrEqual(Effectiveness a, Effectiveness b) {
        if (a.value >= b.value) {
            return true;
        } else {
            return false;
        }
    }

    private static Effectiveness getEffectiveness(Type attackType, Type[] defenderTypes) {
        // TODO: use OPTIONAL instead
        Type firstType, secondType;
        double effectivenessValue;
        firstType = defenderTypes[0];
        if (defenderTypes.length == 1) {
            effectivenessValue = TypeStore.getEffectiveness(firstType.name, attackType.name).value;
        }

        else {
            secondType = defenderTypes[1];
            effectivenessValue = TypeStore.getEffectiveness(firstType.name, attackType.name).value
                    + TypeStore.getEffectiveness(secondType.name, attackType.name).value;
        }
        return Effectiveness.findKeyFromValue(effectivenessValue);
    }

    private int blueExhaustionBonus() {
        int blueExhaustioDiff = this.blue.getExhaustion() - this.red.getExhaustion();
        boolean blueIsExhausted = blueExhaustioDiff > 0;
        if (blueIsExhausted) {
            return -(int) Math.pow(EXHAUSTION_WEIGHT_BASE, blueExhaustioDiff);
        } else {
            boolean redIsExhausted = blueExhaustioDiff < 0;
            if (redIsExhausted) {
                return (int) Math.pow(EXHAUSTION_WEIGHT_BASE, -blueExhaustioDiff);
            }
        }
        return 0;
    }

    private boolean blueWinsRandomly() {
        boolean blueWins = Math.random() < 0.5;
        return blueWins;
    }

    private double blueStatBonus() {
        return this.blueSumDiff();
    }

    private double blueSumDiff() {
        int sumRed = this.red.getStatSum();
        int sumBlue = this.blue.getStatSum();
        return Math.round((sumBlue - sumRed) * STAT_WEIGHT);
    }
}

class AttackText {

    public static String buildString(String attacker, AttackNameAndEffect move) {
        String effectString;
        switch (move.effect) {
            case IMMUN:
                effectString = "Nothing happens!";
                break;
            case VERY:
                effectString = "It's very effective!";
                break;
            case SUPER:
                effectString = "It's super effective!";
                break;
            case SUPER_BAD:
                effectString = "It's nearly ineffective!";
                break;
            case RESISTANT:
                effectString = "It's not very effective!";
                break;
            default:
                effectString = "";
                break;
        }
        return attacker + " uses " + move.attack + "! " + effectString;
    }

}

class AttackNameAndEffect {
    public String attack;
    public Effectiveness effect;

    AttackNameAndEffect(String attack, Effectiveness effect, String languageParam) {
        this.attack = Translater.getTranslatedAttack(attack, languageParam);
        this.effect = effect;
    }
}

class CombatResult {
    public boolean blueWin;
    public Stack<String> texts;

    CombatResult(boolean blueWin, Stack<String> texts) {
        this.texts = texts;
        this.blueWin = blueWin;
    }
}
