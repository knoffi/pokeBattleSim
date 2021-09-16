package com.example.demo.Combat.PreCombat;

import java.util.Optional;
import java.util.Stack;
import java.util.stream.IntStream;

import com.example.demo.Combat.Logs.AttackLog;
import com.example.demo.Combat.Logs.CombatLog;
import com.example.demo.Combat.Logs.SpeedContestLog;
import com.example.demo.Combat.Logs.StatChangeLog;
import com.example.demo.Combat.Logs.StatusEffectLog;
import com.example.demo.Combat.Logs.StatusLog;
import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Pokemon.Attack;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.Pokemon.StatKeys;
import com.example.demo.Pokemon.Status.StatusKeys;

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
        CombatLog winnerAnnouncement = new SpeedContestLog(this.blueIsFaster, fastPokemon.getName(), this.roundsAhead,
                this.language);
        this.summary.push(winnerAnnouncement);

    }

    private void pushStatAttack(String statChanger) {
        Optional<String> emptyType = Optional.empty();
        CombatLog statAttack = new AttackLog(this.blueIsFaster, this.fastPokemon.getName(), statChanger, this.language,
                emptyType);
        this.summary.push(statAttack);

    }

    private void pushStatEffect(String target, String stat, boolean isRising) {
        CombatLog statEffect = new StatChangeLog(this.blueIsFaster, target, stat, isRising, this.language);
        this.summary.push(statEffect);

    }

    private void pushStatusChange(String status) {
        StatusKeys statusKey = StatusKeys.getKeyFromName(status);

        CombatLog statusChange = new StatusLog(!this.blueIsFaster, this.slowPokemon.getName(), statusKey, language);
        CombatLog statusEffect = new StatusEffectLog(!this.blueIsFaster, this.slowPokemon.getName(), statusKey,
                language);
        this.summary.push(statusChange);
        this.summary.push(statusEffect);

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
        Optional<Attack> changer = fastPokemon.getStatOrStatusChangers().stream().findAny();

        if (changer.isEmpty()) {
            return;
        } else {
            Attack move = changer.get();
            if (move.isPureStatusChanger()) {
                this.applyStatusChanger(move);
                ;
            } else {
                // repeats this process as often as roundsAhead
                this.applyStatChanger(move, roundsAhead);
            }
        }
    }

    private void applyStatusChanger(Attack move) {
        this.slowPokemon.setStatus(move.getAilment());
        this.pushStatAttack(move.getName());
        this.pushStatusChange(move.getAilment());
    }

    private void applyStatChanger(Attack move, int roundsAhead) {
        Pokemon target = move.enemyIsTarget() ? this.slowPokemon : this.fastPokemon;
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