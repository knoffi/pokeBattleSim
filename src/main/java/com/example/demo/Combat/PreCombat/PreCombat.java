package com.example.demo.Combat.PreCombat;

import java.util.Arrays;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.IntStream;

import com.example.demo.Combat.Logs.AttackLog;
import com.example.demo.Combat.Logs.CombatLog;
import com.example.demo.Combat.Logs.SpeedContestLog;
import com.example.demo.Combat.Logs.StatChangeLog;
import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Pokemon.Attack;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.Pokemon.StatKeys;

public class PreCombat {

    private final static int SPEED_STEP = 40;
    private Pokemon fastPokemon;
    private Pokemon slowPokemon;
    private int roundsAhead;
    private boolean blueIsFaster;
    private Stack<CombatLog> summary;
    private Languages language;

    public PreCombat(Pokemon blue, Pokemon red, Languages language) {
        int blueSpeedAdvantage = blue.getStatValue(StatKeys.SPEED) - red.getStatValue(StatKeys.SPEED);
        this.blueIsFaster = blueSpeedAdvantage > 0;
        this.roundsAhead = Math.abs(blueSpeedAdvantage / SPEED_STEP);
        this.fastPokemon = this.blueIsFaster ? blue : red;
        this.slowPokemon = this.blueIsFaster ? red : blue;
        this.summary = new Stack<CombatLog>();
        this.language = language;

    }

    private void pushSpeedWinner() {
        CombatLog winnerAnnouncement = new SpeedContestLog(this.blueIsFaster, fastPokemon.getName(), this.roundsAhead);
        this.summary.push(winnerAnnouncement);

    }

    private void pushStatAttack(String statChanger) {
        Optional<String> emptyType = Optional.empty();
        CombatLog statAttack = new AttackLog(this.blueIsFaster, this.fastPokemon.getName(), statChanger, this.language,
                emptyType);
        this.summary.push(statAttack);

    }

    private void pushStatEffect(String target, String stat, boolean isRising) {
        CombatLog statEffect = new StatChangeLog(this.blueIsFaster, target, stat, isRising);
        this.summary.push(statEffect);

    }

    public Stack<CombatLog> getPreCombatResult() {
        this.pushSpeedWinner();
        this.resolveSpeedAdvantage();
        return this.summary;
    }

    private void resolveSpeedAdvantage() {

        if (this.roundsAhead < 1) {
            return;
        }

        Optional<Attack> statChanger = Arrays.stream(fastPokemon.getPureStatChangers()).findAny();

        if (statChanger.isEmpty()) {
            return;
        } else {
            // repeat this process as often as roundsAhead
            this.applyStatChanger(this.fastPokemon, this.slowPokemon, statChanger.get(), roundsAhead);
        }
    }

    private void applyStatChanger(Pokemon user, Pokemon enemy, Attack move, int roundsAhead) {
        Pokemon target = move.enemyIsTarget() ? enemy : user;
        String changedStat = move.getStatChanges()[0].getName();
        boolean statIsRising = move.getStatChanges()[0].isRisingStat();
        this.pushStatAttack(move.getName());
        this.pushStatEffect(target.getName(), changedStat, statIsRising);

        if (roundsAhead <= 0) {
            try {
                throw new Exception("NonPositiveRoundsAhead");
            } catch (Exception e) {
                System.out.println("___NO STAT CHANGING APPLIED___");
            }
        }
        // repeat stat change for each round ahead
        IntStream.rangeClosed(1, roundsAhead).forEach(step -> target.applyStatChanger(move));
    }

}