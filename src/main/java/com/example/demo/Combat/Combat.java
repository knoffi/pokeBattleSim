package com.example.demo.Combat;

import java.util.Stack;

import com.example.demo.Controller.LogRound;
import com.example.demo.Pokemon.Attack;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.Pokemon.Type;
import com.example.demo.TypeEffects.Effectiveness;
import com.example.demo.TypeEffects.TypeStore;

public class Combat {
    private Pokemon red;
    private Pokemon blue;
    private Stack<String> combatSummary;

    public Combat(Pokemon pokemonRed, Pokemon pokemonBlue) {
        this.red = pokemonRed;
        this.blue = pokemonBlue;
        this.combatSummary = new Stack<String>();
    }

    public LogRound getResult() {
        CombatResult combatResult = new BattleCalculation(this.blue, this.red).getResult();
        boolean blueWins = combatResult.blueWin;
        Pokemon winner = blueWins ? this.blue : this.red;

        winner.addExhaustion();

        this.combatSummary.addAll(combatResult.texts);

        String[] combatSummary = this.combatSummary.toArray(String[]::new);
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

    public BattleCalculation(Pokemon blue, Pokemon red) {
        this.blue = blue;
        this.red = red;
        this.blueAttack = getBestAttackEffect(this.blue.getFinishingBlows(), this.red.getPokeTypes());
        this.redAttack = getBestAttackEffect(this.red.getFinishingBlows(), this.blue.getPokeTypes());
    }

    public CombatResult getResult() {
        double blueVictoryPoints = 0;
        double statBonus = this.blueStatBonus();
        int exhaustBonus = this.blueExhaustionBonus();
        double attackBonus = this.blueAttackBonus();
        System.out.println("stat bonus :" + statBonus);
        System.out.println("exhaust bonus :" + exhaustBonus);
        System.out.println("attack bonus :" + attackBonus);
        blueVictoryPoints += statBonus + exhaustBonus + attackBonus;
        System.out.println("result :" + blueVictoryPoints);
        System.out.println("_________________________________");
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
        Stack<String> texts = new Stack<String>();
        texts.add(blueAttack);
        texts.add(redAttack);
        texts.add(loserName + " was defeated!");
        return texts;

    }

    private static AttackNameAndEffect getBestAttackEffect(Attack[] attacks, Type[] defender) {
        Effectiveness maxEffectiveness = Effectiveness.IMMUN;

        String bestAttackName = "struggle";
        for (Attack attack : attacks) {
            Effectiveness newEffectiveness = getEffectiveness(attack.getType(), defender);
            if (isBiggerOrEqual(newEffectiveness, maxEffectiveness)) {
                bestAttackName = attack.getName();
                maxEffectiveness = newEffectiveness;
            }
        }
        return new AttackNameAndEffect(bestAttackName, maxEffectiveness);
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
                effectString = "Nothing happened!";
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

    AttackNameAndEffect(String attack, Effectiveness effect) {
        this.attack = attack;
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
