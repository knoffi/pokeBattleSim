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
        boolean blueWins = this.blueWonSimulation();
        Stack<String> texts = this.getResultTexts(blueWins);
        return new CombatResult(blueWins, texts);
    }

    // TODO: needs testing
    private boolean blueWonSimulation() {
        double blueAttackValue = this.getAttackValue(this.blueAttack, true);
        double redAttackValue = this.getAttackValue(this.redAttack, false);
        int roundsBlueCanSurvive = (int) (this.blue.getHP() / redAttackValue);
        int roundsRedCanSurvive = (int) (this.red.getHP() / blueAttackValue);
        // TODO: use speed factor here to decide equality case :: I assume here that the
        // winner always had first strike
        boolean blueWon = roundsBlueCanSurvive >= roundsRedCanSurvive;
        if (blueWon) {
            int sufferedDamage = roundsRedCanSurvive * (int) redAttackValue;
            this.blue.takesDamage(sufferedDamage);

            return true;
        } else {
            int sufferedDamage = roundsBlueCanSurvive * (int) blueAttackValue;
            this.red.takesDamage(sufferedDamage);

            return false;
        }
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
        // TODO: test each step of factor calculation with easy examples
        Pokemon attacker = blueAttacks ? this.blue : this.red;
        Pokemon defender = blueAttacks ? this.red : this.blue;

        Effectiveness effect = getEffectiveness(attack.getType(), defender.getPokeTypes());
        int attackStat = attacker.getAttackStat();
        int defenseStat = defender.getDefenseStat();
        int attackerLevel = attacker.getLevel();

        double levelFactor = 2 * attackerLevel / 5.0 + 2;
        double statFactor = attackStat * 1.0 / defenseStat;
        double powerFactor = attack.getPower() / 50.0;
        double randomFactor = (217 + Math.random() * 38) / 255;
        double effectFactor = effect.value;
        // -> getBestAttackEffect
        return (levelFactor * statFactor * powerFactor + 2) * randomFactor * effectFactor;
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
}

class CombatResult {
    public boolean blueWin;
    public Stack<String> texts;

    CombatResult(boolean blueWin, Stack<String> texts) {
        this.texts = texts;
        this.blueWin = blueWin;
    }
}
