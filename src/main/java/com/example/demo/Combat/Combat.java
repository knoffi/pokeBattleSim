package com.example.demo.Combat;

import java.util.Arrays;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.IntStream;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Controller.LogRound;
import com.example.demo.Pokemon.Attack;
import com.example.demo.Pokemon.DamageClass;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.Pokemon.StatKeys;
import com.example.demo.Pokemon.Type;
import com.example.demo.TypeEffects.Effectiveness;
import com.example.demo.TypeEffects.TypeStore;

public class Combat {
    private Pokemon red;
    private Pokemon blue;
    private Stack<CombatLog> combatSummary;
    private Languages language;

    public Combat(Pokemon pokemonRed, Pokemon pokemonBlue, Languages language) {
        this.red = pokemonRed;
        this.blue = pokemonBlue;
        this.language = language;
        this.combatSummary = new Stack<CombatLog>();

        this.pushPokemonSummons();
    }

    private void pushPokemonSummons() {
        SummonLog blueSummon = new SummonLog(true, this.blue.getName());
        SummonLog redSummon = new SummonLog(false, this.red.getName());
        this.combatSummary.add(blueSummon);
        this.combatSummary.add(redSummon);
    }

    private void pushFightResult(boolean blueWins) {
        String loserName = blueWins ? this.red.getName() : this.blue.getName();
        ResultLog fightResult = new ResultLog(blueWins, loserName);
        this.combatSummary.push(fightResult);
    }

    public LogRound getResult() {
        final CombatResult combatResult = new BattleCalculation(this.blue, this.red, language).getResult();
        final boolean blueWins = combatResult.blueWin;

        this.pushFightResult(blueWins);

        CombatLog[] combatLogs = this.combatSummary.toArray(CombatLog[]::new);
        return new LogRound(this.red.getName(), this.blue.getName(), combatLogs, blueWins);
    }

}

class BattleCalculation {
    private final static int SPEED_STEP = 40;

    private Pokemon blue;
    private Pokemon red;
    private Attack blueAttack;
    private Attack redAttack;
    private Effectiveness blueEffect;
    private Effectiveness redEffect;
    private Languages language;
    private Stack<CombatLog> combatSummary;

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
        this.pushCombatTexts(blueWins);
        return new CombatResult(blueWins, this.combatSummary);
    }

    private boolean blueWonSimulation() {
        int blueSpeedAdvantage = this.blue.getStatValue(StatKeys.SPEED) - this.red.getStatValue(StatKeys.SPEED);

        this.resolveSpeedAdvantage(blueSpeedAdvantage);
        boolean blueStarted = blueSpeedAdvantage >= 0;

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

        return blueWon;
    }

    // TODO: make this into a "preBattle" and store results for text display and
    // further
    // combat
    private void resolveSpeedAdvantage(int blueSpeedAdvantage) {
        int speedDiff = Math.abs(blueSpeedAdvantage);

        if (speedDiff < 1 * SPEED_STEP) {
            return;
        }

        boolean blueIsFaster = blueSpeedAdvantage > 0;
        Pokemon fastPokemon = blueIsFaster ? this.blue : this.red;
        Pokemon slowPokemon = blueIsFaster ? this.red : this.blue;

        int roundsAhead = speedDiff / SPEED_STEP;

        Attack[] statChangers = fastPokemon.getPureStatChangers();

        if (statChangers.length == 0) {
            return;
        } else {
            // repeat this process as often as roundsAhead
            IntStream.rangeClosed(1, roundsAhead)
                    .forEach(step -> this.applyStatChangers(fastPokemon, slowPokemon, statChangers));
        }
    }

    private void applyStatChangers(Pokemon user, Pokemon enemy, Attack[] statChangers) {
        Optional<Attack> randomMove = Arrays.stream(statChangers).findAny();

        if (randomMove.isEmpty()) {
            try {
                throw new Exception("EmptyStatChangers");
            } catch (Exception e) {
                System.out.print("___THERE ARE NO STAT CHANGERS TO USE___");
            }
        } else {
            Attack move = randomMove.get();
            Pokemon target = move.enemyIsTarget() ? enemy : user;
            target.applyStatChanger(move);
        }
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
        if (winner.isKO()) {
            try {
                throw new Exception("DeadWinnerGoesToNextRound");
            } catch (Exception e) {
                winner.revive();
                System.out.print("___WINNER GOES WITH 0 HP TO NEXT TO NEXT ROUND___");
            }
        }
    }

    private void pushCombatTexts(boolean blueWon) {
        CombatText text = new CombatText(this.blue.getName(), this.blueAttack.getName(), blueEffect, red.getName(),
                redAttack.getName(), redEffect, this.language, blueWon);

        String blueAttackText = text.getAttackText(true);
        Optional<String> blueAttackType = Optional.of(this.blueAttack.getType().name);
        CombatLog blueAttackLog = new AttackLog(true, blueAttackText, blueAttackType);
        this.combatSummary.push(blueAttackLog);

        String blueEffectText = text.getEffectivenessText(true);
        CombatLog blueEffectLog = new TextLog(true, blueEffectText);
        this.combatSummary.push(blueEffectLog);

        String redAttackText = text.getAttackText(false);
        Optional<String> redAttackType = Optional.of(this.redAttack.getType().name);
        CombatLog redAttackLog = new AttackLog(false, redAttackText, redAttackType);
        this.combatSummary.push(redAttackLog);

        String redEffectText = text.getEffectivenessText(false);
        CombatLog redEffectLog = new TextLog(false, redEffectText);
        this.combatSummary.push(redEffectLog);
    }

    private double getAttackValue(Attack attack, boolean blueAttacks) {
        // TODO: test each step of factor calculation with easy examples
        Pokemon attacker = blueAttacks ? this.blue : this.red;
        Pokemon defender = blueAttacks ? this.red : this.blue;

        Effectiveness effect = getEffectiveness(attack.getType(), defender.getPokeTypes());
        boolean isPhysicalAttack = attack.getDamageClass().equals(DamageClass.PHYSICAL);
        StatKeys attackKey = isPhysicalAttack ? StatKeys.ATT : StatKeys.SPEC_ATT;
        StatKeys defenseKey = isPhysicalAttack ? StatKeys.DEF : StatKeys.SPEC_DEF;

        int attackStat = attacker.getStatValue(attackKey);
        int defenseStat = defender.getStatValue(defenseKey);
        int attackerLevel = attacker.getLevel();

        double levelFactor = 2 * attackerLevel / 5.0 + 2;
        double statFactor = attackStat * 1.0 / defenseStat;
        double powerFactor = attack.getPower() / 50.0;
        double randomFactor = (217 + Math.random() * 38) / 255;
        double effectFactor = effect.value;
        double sameTypeFactor = this.getSameTypeFactor(attack.getType(), attacker);
        // -> getBestAttackEffect
        return (levelFactor * statFactor * powerFactor + 2) * randomFactor * effectFactor * sameTypeFactor;
    }

    private double getSameTypeFactor(Type type, Pokemon attacker) {

        boolean attackerSharesType = Arrays.stream(attacker.getPokeTypes())
                .anyMatch(pokeType -> type.name.equals(pokeType.name));

        if (attackerSharesType) {
            return 1.5;
        } else {
            return 1.0;
        }
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
    public Stack<CombatLog> texts;

    CombatResult(boolean blueWin, Stack<CombatLog> texts) {
        this.texts = texts;
        this.blueWin = blueWin;
    }
}
