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

    private static final int DEFAULT_HP = 110;
    private static final int DEFAULT_STAT = 40;
    private static final int MAXIMAL_LEVEL = 100;
    private static final int MAXIMAL_EFFORT_VALUE = 265;

    private String name;
    private Stat[] stats;
    private Type[] types;
    private Attack[] attacks;
    private String frontSpriteUrl;
    private String backSpriteUrl;
    private int exhaustionPoint;
    private int level = MAXIMAL_LEVEL;
    private int effortValue = MAXIMAL_EFFORT_VALUE;
    private int HP;

    public Pokemon(PokemonSearch data) {
        this.name = data.name;
        this.stats = Arrays.stream(data.stats).map(StatBySearch::convert).toArray(Stat[]::new);
        this.types = Arrays.stream(data.types).map(TypeHolder::convert).toArray(Type[]::new);
        this.attacks = creatAttacks(data.moves);
        this.backSpriteUrl = data.sprites.back_default;
        this.frontSpriteUrl = data.sprites.front_default;
        this.exhaustionPoint = 0;
        this.HP = this.getStartHP();
    }

    public int getStatSum() {
        var statValues = Arrays.stream(this.stats).map(stat -> stat.value);
        int sum = statValues.reduce(0, (cur, prev) -> cur + prev);
        return sum;
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

    private int getBaseAttackStat(boolean forPhysical) throws Exception {
        String statKey = forPhysical ? "attack" : "special-attack";
        Optional<Stat> attackStat = Arrays.stream(this.stats).filter(stat -> stat.name.equals(statKey)).findAny();
        if (attackStat.isPresent()) {
            return attackStat.get().value;
        } else {
            throw new Exception("NoAttackStatFound");
        }
    }

    private int getBaseHPStat() throws Exception {
        String statKey = "hp";
        Optional<Stat> attackStat = Arrays.stream(this.stats).filter(stat -> stat.name.equals(statKey)).findAny();
        if (attackStat.isPresent()) {
            return attackStat.get().value;
        } else {
            throw new Exception("NoHPStatFound");
        }
    }

    private int getBaseSpeedStat() throws Exception {
        String statKey = "speed";
        Optional<Stat> attackStat = Arrays.stream(this.stats).filter(stat -> stat.name.equals(statKey)).findAny();
        if (attackStat.isPresent()) {
            return attackStat.get().value;
        } else {
            throw new Exception("NoSpeedStatFound");
        }
    }

    private int getBaseDefenseStat(boolean forPhysical) throws Exception {
        String statKey = forPhysical ? "defense" : "special-defense";
        Optional<Stat> defenseStat = Arrays.stream(this.stats).filter(stat -> stat.name.equals(statKey)).findAny();
        if (defenseStat.isPresent()) {
            return defenseStat.get().value;
        } else {
            throw new Exception("NoDefenseStatFound");
        }
    }

    public int getAttackStat(boolean isPhysical) {
        try {
            int baseValue = this.getBaseAttackStat(isPhysical);
            int levelValue = this.calculateStatFromLevel(baseValue, false);
            return levelValue;
        } catch (Exception e) {
            System.out.println("___NO ATTACK STAT FOUND FOR " + this.name + "___");
        }
        return DEFAULT_STAT;
    }

    public int getSpeedStat() {
        try {
            int baseValue = this.getBaseSpeedStat();
            int levelValue = this.calculateStatFromLevel(baseValue, false);
            return levelValue;
        } catch (Exception e) {
            System.out.println("___NO SPEED STAT FOUND FOR " + this.name + "___");
        }
        return DEFAULT_STAT;
    }

    private int getStartHP() {
        try {
            int baseValue = this.getBaseHPStat();
            int levelValue = this.calculateStatFromLevel(baseValue, true);
            return levelValue;
        } catch (Exception e) {
            System.out.println("___NO HP STAT FOUND FOR " + this.name + "___");
        }
        return DEFAULT_HP;
    }

    public int getDefenseStat(boolean isPhysical) {
        try {
            int baseValue = this.getBaseDefenseStat(isPhysical);
            int levelValue = this.calculateStatFromLevel(baseValue, false);
            return levelValue;
        } catch (Exception e) {
            System.out.println("___NO DEFENSE STAT FOUND FOR " + this.name + "___");
        }
        return DEFAULT_STAT;
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

    private int calculateStatFromLevel(int baseValue, boolean isAboutHP) {
        double mainFactor = (baseValue * 2 + Math.floor(this.effortValue / 4.0)) / 100.0 * this.level;
        int summandOffSet = isAboutHP ? this.level + 10 : 5;
        return (int) mainFactor + summandOffSet;
    }

    public Attack[] getFinishingBlows() {
        Attack[] finishingAttacks = Arrays.stream(this.attacks).filter(Attack::doesDamage).toArray(Attack[]::new);
        return finishingAttacks;
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
        String attackPath = Pokedex.getPathFromURL(URL);
        try {
            MoveSearch move = Pokedex.getPokeData(attackPath, MoveSearch.class, RequestMode.JAVA_11);
            Attack attack = move.convert();
            return attack;
        } catch (IOException | InterruptedException e) {
            System.out.println("___ATTACK BUILDING FAILED___" + e.getClass());
        }
        return new Attack();
    }

}
