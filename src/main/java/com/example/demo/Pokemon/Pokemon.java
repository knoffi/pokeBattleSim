package com.example.demo.Pokemon;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.Stack;

import com.example.demo.RequestMode;
import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Controller.LogPokemon;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Pokemon.Status.Status;
import com.example.demo.Pokemon.Status.StatusKeys;
import com.example.demo.Searches.MoveSearch.MoveSearch;
import com.example.demo.Searches.PokemonSearch.MoveBySearch;
import com.example.demo.Searches.PokemonSearch.PokemonSearch;
import com.example.demo.Searches.PokemonSearch.StatBySearch;
import com.example.demo.Searches.PokemonSearch.TypeHolder;
import com.example.demo.Translater.Translater;
import com.example.demo.TypeEffects.Effectiveness;

public class Pokemon {

    private static final int MAXIMAL_ATTACK_AMOUNT = 6;

    private static final int DEFAULT_HP = 150;
    private static final int DEFAULT_STAT = 100;
    private static final int MAXIMAL_LEVEL = 100;

    private String name;
    private HashMap<String, Stat> stats;
    private Type[] types;
    private Attack[] attacks;
    private String frontSpriteUrl;
    private String backSpriteUrl;
    private int level = MAXIMAL_LEVEL;
    private int HP;
    private Status status;

    public Pokemon(PokemonSearch data, Languages language) {
        this.name = Translater.getTranslatedName(data.name, language);
        this.stats = createStatMap(data.stats);
        this.types = Arrays.stream(data.types).map(TypeHolder::convert).toArray(Type[]::new);
        this.attacks = createAttacks(data.moves);
        this.backSpriteUrl = data.sprites.back_default;
        this.frontSpriteUrl = data.sprites.front_default;
        this.HP = this.getStatValue(StatKeys.HP);
        this.status = new Status();
    }

    public int getLevel() {
        return this.level;
    }

    public Type[] getPokeTypes() {
        return this.types;
    }

    public LogPokemon getLogData() {

        return new LogPokemon(this.name, this.backSpriteUrl, this.frontSpriteUrl);
    }

    public String getName() {
        return this.name;
    }

    public void applyStatChanger(Attack move) {
        if (move.isPureStatChanger()) {
            StatChange[] changes = move.getStatChanges();
            Arrays.stream(changes).forEach(change -> this.changeStat(change));

        } else {
            try {
                throw new Exception("InvalidStatChanger");
            } catch (Exception e) {
                System.out.println("___EXSPECTED A PURE STAT CHANGER___");
            }
        }
    }

    private static HashMap<String, Stat> createStatMap(StatBySearch[] preStats) {
        HashMap<String, Stat> newMap = new HashMap<String, Stat>();
        Arrays.stream(preStats).map(StatBySearch::convert).forEach(stat -> newMap.put(stat.name, stat));
        newMap.put(StatKeys.EVA.name, new Stat(StatKeys.EVA.name, 100));
        newMap.put(StatKeys.ACC.name, new Stat(StatKeys.ACC.name, 100));
        return newMap;

    }

    private void changeStat(StatChange change) {
        try {
            Optional<StatKeys> key = Arrays.stream(StatKeys.values())
                    .filter(statKey -> statKey.name.equals(change.stat)).findFirst();
            if (key.isEmpty()) {
                throw new Exception("StatKeyNotFound");
            } else {
                Stat targetedStat = this.getStatFromKey(key.get());
                targetedStat.changeModifier(change.value);
            }
        } catch (Exception e) {
            System.out.println("___STAT " + change.stat + " WAS NOT FOUND FOR CHANGE___");
        }
    }

    public int getStatValue(StatKeys key) {
        try {
            int levelValue = this.getStatFromKey(key).getValue(this.level);
            return levelValue;
        } catch (Exception e) {
            System.out.println("___NO " + key.name + " STAT FOUND FOR " + this.name + "___");
        }
        boolean isForHP = key == StatKeys.HP;
        return getDefaultValue(isForHP);
    }

    private int getDefaultValue(boolean isForHP) {
        if (isForHP) {
            return DEFAULT_HP;
        } else {
            return DEFAULT_STAT;
        }

    }

    private Stat getStatFromKey(StatKeys key) throws Exception {
        Stat desiredStat = this.stats.get(key.name);

        if (desiredStat == null) {
            throw new Exception(key + "NotFound");
        } else {
            return desiredStat;
        }
    }

    public int getHP() {
        return this.HP;
    }

    public void setKO() {
        this.HP = 0;
    }

    public void revive() {
        if (this.HP == 0) {
            this.HP = 1;
        }
    }

    public Optional<StatusKeys> takesDamage(int damage, Effectiveness effect) {
        this.inflictDamageByStatus();
        this.HP -= damage;
        return this.handleStatusDuration(effect);

    }

    private void inflictDamageByStatus() {
        if (this.status.dotDamage > 0) {
            this.HP -= (int) (this.status.dotDamage * this.HP);
        }
    }

    public double dealsDamage(double damage) {
        this.HP -= this.status.selfHarm * damage;
        return (1 - this.status.damageReduce) * damage;
    }

    private Optional<StatusKeys> handleStatusDuration(Effectiveness damage) {
        switch (damage) {
            case IMMUN:
                return this.resolveStatusCounter(Status.SLEEP_ROUNDS / 1);
            case SUPER_BAD:
                return this.resolveStatusCounter(Status.SLEEP_ROUNDS / 1);
            case RESISTANT:
                return this.resolveStatusCounter(Status.SLEEP_ROUNDS / 1);
            case NORMAL:
                return this.resolveStatusCounter(Status.SLEEP_ROUNDS / 2);
            case VERY:
                return this.resolveStatusCounter(Status.SLEEP_ROUNDS / 3);
            case SUPER:
                return this.resolveStatusCounter(Status.SLEEP_ROUNDS / 3);
        }
        return Optional.empty();
    }

    private Optional<StatusKeys> resolveStatusCounter(int counters) {
        this.status.roundsLeft -= counters;
        if (this.status.roundsLeft <= 0) {
            Optional<StatusKeys> endedStatus = Optional.of(this.status.key);
            this.status = new Status();
            return endedStatus;
        }
        return Optional.empty();
    }

    public boolean isKO() {
        return this.HP <= 0;
    }

    public boolean canFight() {
        return this.HP > 0;
    }

    public Attack[] getFinishingBlows() {
        Attack[] finishingAttacks = Arrays.stream(this.attacks).filter(Attack::doesDamage).toArray(Attack[]::new);
        return finishingAttacks;
    }

    public Stack<Attack> getStatOrStatusChangers() {
        Stack<Attack> pureChangers = new Stack<Attack>();
        Arrays.stream(this.attacks).filter(Attack::isPureChanger).forEach(changer -> pureChangers.push(changer));
        return pureChangers;
    }

    public void setStatus(String status) {
        this.status = new Status(status);
    }

    private static int[] getMoveSelection(int maxIndex) {
        int selectionSize = Math.min(MAXIMAL_ATTACK_AMOUNT, maxIndex + 1);
        int[] randomMoveIndices = new int[selectionSize];
        for (int i = 0; i < selectionSize; i++) {
            int randomIndex;
            do {

                randomIndex = (int) Math.round(Math.random() * maxIndex);
            } while (moveIndexTaken(randomMoveIndices, randomIndex, i));
            randomMoveIndices[i] = randomIndex;
        }
        return randomMoveIndices;
    }

    private static boolean moveIndexTaken(int[] indices, int newIndex, int trimIndex) {
        return Arrays.stream(indices).limit(trimIndex).anyMatch(index -> index == newIndex);
    }

    private Attack[] createAttacks(MoveBySearch[] moves) {
        String[] filteredURLs = Arrays.stream(moves).filter(MoveBySearch::isSupported).map(move -> move.move.url)
                .toArray(String[]::new);
        int moveAmount = filteredURLs.length;
        int[] selectedIndices = getMoveSelection(moveAmount - 1);
        int selectionSize = Math.min(MAXIMAL_ATTACK_AMOUNT, moveAmount);
        Attack[] selectedAttacks = new Attack[selectionSize];
        try {
            for (int k = 0; k < selectionSize; k++) {
                int selectedIndex = selectedIndices[k];
                String selectedURL = filteredURLs[selectedIndex];
                selectedAttacks[k] = createAttack(selectedURL);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("___INDEX OUT OF BOUNCE DURING ATTACK SELECTION___" + e.getClass());
        }
        ;

        return selectedAttacks;
    }

    private Attack createAttack(String URL) {
        try {
            MoveSearch move = Pokedex.getPokeDataByURL(URL, MoveSearch.class, RequestMode.JAVA_11);
            Attack attack = move.convert();
            return attack;
        } catch (IOException | InterruptedException e) {
            System.out.println("___ATTACK BUILDING FAILED___" + e.getClass());
        }
        return new Attack();
    }

}
