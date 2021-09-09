package com.example.demo.Pokemon;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import com.example.demo.RequestMode;
import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Controller.LogPokemon;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.MoveSearch.MoveSearch;
import com.example.demo.Searches.PokemonSearch.MoveBySearch;
import com.example.demo.Searches.PokemonSearch.PokemonSearch;
import com.example.demo.Searches.PokemonSearch.StatBySearch;
import com.example.demo.Searches.PokemonSearch.TypeHolder;
import com.example.demo.Translater.Translater;

public class Pokemon {

    private static final int MAXIMAL_ATTACK_AMOUNT = 6;

    private static final int DEFAULT_HP = 150;
    private static final int DEFAULT_STAT = 100;
    private static final int MAXIMAL_LEVEL = 100;

    private String name;
    private Stat[] stats;
    private Type[] types;
    private Attack[] attacks;
    private String frontSpriteUrl;
    private String backSpriteUrl;
    private int exhaustionPoint;
    private int level = MAXIMAL_LEVEL;
    private int HP;

    public Pokemon(PokemonSearch data) {
        this.name = data.name;
        this.stats = Arrays.stream(data.stats).map(StatBySearch::convert).toArray(Stat[]::new);
        this.types = Arrays.stream(data.types).map(TypeHolder::convert).toArray(Type[]::new);
        this.attacks = creatAttacks(data.moves);
        this.backSpriteUrl = data.sprites.back_default;
        this.frontSpriteUrl = data.sprites.front_default;
        this.exhaustionPoint = 0;
        this.HP = this.getStatValue(StatKeys.HP);
    }

    public void translateName(Languages language) {
        this.name = Translater.getTranslatedName(this.name, language);
    }

    public int getExhaustion() {
        return this.exhaustionPoint;
    }

    public void addExhaustion() {
        this.exhaustionPoint++;
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
        Optional<Stat> desiredStat = Arrays.stream(this.stats).filter(stat -> stat.name.equals(key.name)).findAny();

        if (desiredStat.isEmpty()) {
            throw new Exception(key + "NotFound");
        } else {
            return desiredStat.get();
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

    public int takesDamage(int damage) {
        return this.HP -= damage;
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

    public Attack[] getPureStatChangers() {
        Attack[] pureStatChangers = Arrays.stream(this.attacks).filter(Attack::isPureStatChanger)
                .toArray(Attack[]::new);
        return pureStatChangers;
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

    private Attack[] creatAttacks(MoveBySearch[] moves) {
        String[] filteredURLs = Arrays.stream(moves).filter(MoveBySearch::isSupported).map(move -> move.move.url)
                .toArray(String[]::new);
        int moveAmount = filteredURLs.length;
        int[] selectedIndices = getMoveSelection(moveAmount - 1);
        // TODO: throw exception and use "Verzweifler" if selectionSize is less than 1
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
