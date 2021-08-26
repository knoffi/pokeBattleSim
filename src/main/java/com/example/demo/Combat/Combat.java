package com.example.demo.Combat;

import java.util.Arrays;
import java.util.Optional;
import java.util.Stack;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Controller.LogRound;
import com.example.demo.Pokemon.Attack;
import com.example.demo.Pokemon.DamageClass;
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
        boolean blueStarted = this.blue.getSpeedStat() >= this.red.getSpeedStat();
        double blueAttackValue = this.getAttackValue(this.blueAttack, true);
        double redAttackValue = this.getAttackValue(this.redAttack, false);

        int roundsBlueCanSurvive = this.getSurvivableRounds(this.blue.getHP(), redAttackValue);
        int roundsRedCanSurvive = this.getSurvivableRounds(this.red.getHP(), blueAttackValue);

        boolean blueWonClearly = roundsBlueCanSurvive > roundsRedCanSurvive;
        boolean blueWonBarely = roundsBlueCanSurvive == roundsRedCanSurvive && blueStarted;
        boolean blueWon = blueWonBarely || blueWonClearly;

        Pokemon winner = blueWon ? this.blue : this.red;
        Pokemon loser = blueWon ? this.red : this.blue;
        int loserSurvivedRounds = blueWon ? roundsRedCanSurvive : roundsBlueCanSurvive;
        double loserAttackValue = blueWon ? redAttackValue : blueAttackValue;
        boolean winnerHadFirstStrike = blueWon ? blueStarted : !blueStarted;

        this.dealDamage(winner, loser, loserSurvivedRounds, loserAttackValue, winnerHadFirstStrike);
        return blueWonBarely || blueWonClearly;
    }

    private int getSurvivableRounds(int defenderHP, double attackerDamage) {
        int rounds = (int) (defenderHP / attackerDamage);
        int remainingHP = (int) (defenderHP - rounds * attackerDamage);
        if (remainingHP == 0) {
            System.out.println("Edge case can really happen!");
            return rounds - 1;
        } else {
            return rounds;
        }

    }

    private void dealDamage(Pokemon winner, Pokemon loser, int loserSurvivedRounds, double loserAttack,
            boolean winnerHadFirstStrike) {
        loser.setKO();
        int loserLandedHits = loserSurvivedRounds + (winnerHadFirstStrike ? 0 : 1);
        int sufferedDamage = loserLandedHits * (int) loserAttack;
        winner.takesDamage(sufferedDamage);
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
        boolean isPhysicalAttack = attack.getDamageClass().equals(DamageClass.PHYSICAL);
        int attackStat = attacker.getAttackStat(isPhysicalAttack);
        int defenseStat = defender.getDefenseStat(isPhysicalAttack);
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
