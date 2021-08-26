package com.example.demo.Combat;

import java.util.Arrays;
import java.util.Optional;
import java.util.Stack;

import com.example.demo.Combat.PhraseStore.Languages;
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
    private Languages language;

    public Combat(Pokemon pokemonRed, Pokemon pokemonBlue, Languages language) {
        this.red = pokemonRed;
        this.blue = pokemonBlue;
        this.combatSummary = new Stack<String>();
        this.language = language;
    }

    public LogRound getResult() {
        final CombatResult combatResult = new BattleCalculation(this.blue, this.red, language).getResult();
        final boolean blueWins = combatResult.blueWin;
        final Pokemon winner = blueWins ? this.blue : this.red;

        winner.addExhaustion();

        this.combatSummary.addAll(combatResult.texts);

        final String[] combatSummary = this.combatSummary.toArray(String[]::new);
        return new LogRound(this.red.getName(), this.blue.getName(), combatSummary, blueWins);
    }

}

class BattleCalculation {

    private Pokemon blue;
    private Pokemon red;
    private Attack blueAttack;
    private Attack redAttack;
    private Effectiveness blueEffect;
    private Effectiveness redEffect;
    private Languages language;

    public BattleCalculation(Pokemon blue, Pokemon red, Languages language) {
        this.blue = blue;
        this.red = red;
        this.blueAttack = this.getBestAttack(this.blue.getFinishingBlows(), true);
        this.redAttack = this.getBestAttack(this.red.getFinishingBlows(), false);
        this.blueEffect = this.getEffectiveness(this.blueAttack.getType(), red.getPokeTypes());
        this.redEffect = this.getEffectiveness(this.redAttack.getType(), blue.getPokeTypes());
        this.language = language;
    }

    public CombatResult getResult() {

        boolean blueWins = this.blueWinsRandomly();
        Stack<String> texts = this.getResultTexts(blueWins);
        return new CombatResult(blueWins, texts);
    }

    private Stack<String> getResultTexts(boolean blueWon) {
        CombatText text = new CombatText(this.blue.getName(), this.blueAttack.getName(), blueEffect, red.getName(),
                redAttack.getName(), redEffect, this.language, blueWon);
        String blueAttack = text.getAttackText(true);
        String redAttack = text.getAttackText(false);
        String endResult = text.getResultText();

        Stack<String> texts = new Stack<String>();
        texts.add(blueAttack);
        texts.add(redAttack);
        texts.add(endResult);
        return texts;

    }

    private double getAttackValue(Attack attack, boolean blueAttacks) {
        return 10;
    }

    private Attack getBestAttack(Attack[] attacks, boolean blueAttacks) {
        Optional<Attack> bestAttack = Arrays.stream(attacks).max((a, b) -> this.compare(a, b, blueAttacks));
        if (bestAttack.isPresent()) {
            return bestAttack.get();
        } else {
            try {
                throw new Exception("AttacksAreEmpty");
            } catch (Exception e) {
                String attackerName = blueAttacks ? this.blue.getName() : this.red.getName();
                System.out.println("___NO BEST ATTACK FOUND FOR " + attackerName + "___");
            }
            return new Attack();
        }

    }

    private int compare(Attack a, Attack b, boolean blueAttacks) {
        double valueA = this.getAttackValue(a, blueAttacks);
        double valueB = this.getAttackValue(b, blueAttacks);

        return (int) (valueA - valueB);
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
                    * TypeStore.getEffectiveness(secondType.name, attackType.name).value;
        }
        return Effectiveness.findKeyFromValue(effectivenessValue);
    }

    private boolean blueWinsRandomly() {
        boolean blueWins = Math.random() < 0.5;
        return blueWins;
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
