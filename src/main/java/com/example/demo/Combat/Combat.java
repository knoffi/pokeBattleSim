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
    private Pokemon blue;
    private Pokemon red;

    public BattleCalculation(Pokemon blue, Pokemon red) {
        this.blue = blue;
        this.red = red;
    }

    public CombatResult getResult() {
        int blueVictoryPoints = 0;
        blueVictoryPoints += this.blueStatBonus();
        blueVictoryPoints += this.blueExhaustionBonus();
        boolean blueWins = blueVictoryPoints != 0 ? blueVictoryPoints > 0 : this.blueWinsRandomly();
        final Stack<String> texts = this.getResultTexts(blueWins);
        return new CombatResult(blueWins, texts);
    }

    private Stack<String> getResultTexts(boolean blueWins) {
        final String blueAttack = this.getAttackText(true);
        final String redAttack = this.getAttackText(false);
        final String loserName = blueWins ? this.red.getName() : this.blue.getName();
        Stack<String> texts = new Stack<String>();
        texts.add(blueAttack);
        texts.add(redAttack);
        texts.add(loserName + " was defeated!");
        return texts;

    }

    private String getAttackText(boolean blueAttacks) {
        Pokemon attacker = blueAttacks ? this.blue : this.red;
        Pokemon defender = blueAttacks ? this.red : this.blue;
        AttackNameAndEffect data = this.getBestAttackEffect(attacker.getFinishingBlows(), defender.getPokeTypes());
        return new AttackText(attacker.getName(), data.attack, data.effect).buildText();
    }

    private AttackNameAndEffect getBestAttackEffect(Attack[] attacks, Type[] defender) {
        Effectiveness bestEffectiveness = Effectiveness.IMMUN;
        String bestAttackName = "struggle";
        for (Attack attack : attacks) {
            Effectiveness newEffectiveness = this.getMax(bestEffectiveness, attack.getType(), defender);
            if (newEffectiveness.value >= bestEffectiveness.value) {
                bestAttackName = attack.getName();
                bestEffectiveness = newEffectiveness;
            }
        }
        return new AttackNameAndEffect(bestAttackName, bestEffectiveness);
    }

    private Effectiveness getMax(Effectiveness prevEffectiveness, Type attackType, Type[] defenderTypes) {
        Effectiveness attackEffect = this.getEffectiveness(attackType, defenderTypes);
        if (prevEffectiveness.value > attackEffect.value) {
            return prevEffectiveness;
        } else {
            return attackEffect;
        }
    }

    private Effectiveness getEffectiveness(Type attackType, Type[] defenderTypes) {
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
            return -(int) Math.pow(2, blueExhaustioDiff);
        } else {
            boolean redIsExhausted = blueExhaustioDiff < 0;
            if (redIsExhausted) {
                return (int) Math.pow(2, -blueExhaustioDiff);
            }
        }
        return 0;
    }

    private boolean blueWinsRandomly() {
        boolean blueWins = Math.random() < 0.5;
        return blueWins;
    }

    private int blueStatBonus() {
        return this.blueSumDiff();
    }

    private int blueSumDiff() {
        int sumRed = this.red.getStatSum();
        int sumBlue = this.blue.getStatSum();
        return (int) Math.round((sumBlue - sumRed) / 15);
    }
}

class AttackText {
    private Effectiveness effect;
    private String attack;
    private String attacker;

    AttackText(String attacker, String attack, Effectiveness effect) {
        this.effect = effect;
        this.attack = attack;
        this.attacker = attacker;
    }

    public String buildText() {
        String effectString;
        switch (this.effect) {
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
        return this.attacker + " uses " + this.attack + "! " + effectString;
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
