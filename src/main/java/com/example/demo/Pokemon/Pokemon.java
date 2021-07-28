package com.example.demo.Pokemon;

import java.io.IOException;
import java.util.Arrays;

import com.example.demo.RequestMode;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.MoveSearch.MoveSearch;
import com.example.demo.Searches.PokemonSearch.MoveBySearch;
import com.example.demo.Searches.PokemonSearch.PokemonSearch;
import com.example.demo.Searches.PokemonSearch.StatBySearch;
import com.example.demo.Searches.PokemonSearch.TypeHolder;

public class Pokemon {
    private String name;
    private Stat[] stats;
    private Type[] types;
    private Attack[] attacks;

    public Pokemon(PokemonSearch data) {
        this.name = data.name;
        this.stats = Arrays.stream(data.stats).map(StatBySearch::convert).toArray(Stat[]::new);
        this.types = Arrays.stream(data.types).map(TypeHolder::convert).toArray(Type[]::new);
        this.attacks = getAttacks(data.moves);
    }

    public void print() {
        String namePrinter = "I am " + this.name + "\n";
        String statPrinter = "stats:\n";
        for (int i = 0; i < this.stats.length; i++) {
            statPrinter += this.stats[i].name + " " + this.stats[i].value + "\n";
        }
        String typePrinter = this.types.length + " my types:\n";
        for (int i = 0; i < this.types.length; i++) {
            statPrinter += this.types[i].name + "(t)\n";
        }
        String attackPrinter = this.attacks.length + " my attacks:\n";
        for (int i = 0; i < this.attacks.length; i++) {
            statPrinter += this.attacks[i].name + " " + this.attacks[i].meta.category + "(a)\n";
        }
        String completePrinter = namePrinter + statPrinter + typePrinter + attackPrinter;
        System.out.println(completePrinter);

    }

    private static int[] getMoveSelection(int max) {
        int selectionSize = Math.min(4, max);
        int[] randomMoveIndices = new int[selectionSize];
        for (int i = 0; i < selectionSize; i++) {
            int randomIndex;
            do {
                randomIndex = (int) Math.round(Math.random() * max);
            } while (moveIndexTaken(randomMoveIndices, randomIndex));
            randomMoveIndices[i] = randomIndex;
        }
        return randomMoveIndices;
    }

    private static boolean moveIndexTaken(int[] indices, int newIndex) {
        return Arrays.stream(indices).anyMatch(index -> index == newIndex);
    }

    private Attack[] getAttacks(MoveBySearch[] moves) {
        String[] filteredURLs = Arrays.stream(moves).filter(MoveBySearch::isClassical).map(move -> move.move.url)
                .toArray(String[]::new);
        System.out.println(filteredURLs.length);
        int moveAmount = filteredURLs.length;
        int[] selectedIndices = getMoveSelection(moveAmount);
        // TODO: throw exception and use "Verzweifler" if selectionSize is less than 1
        int selectionSize = Math.min(4, moveAmount);
        Attack[] selectedAttacks = new Attack[selectionSize];
        for (int i = 0; i < selectionSize; i++) {
            int selectedIndex = selectedIndices[i];
            String selectedURL = filteredURLs[selectedIndex];
            selectedAttacks[i] = getAttack(selectedURL);
        }
        return selectedAttacks;
    }

    static private Attack getAttack(String URL) {
        String attackPath = Pokedex.getPathFromURL(URL);
        try {
            Attack attack = Pokedex.getPokeData(attackPath, MoveSearch.class, RequestMode.JAVA_11).convert();
            return attack;
        } catch (IOException | InterruptedException e) {
            System.out.println("___ATTACK BUILDING FAILED___" + e.getClass());
        }
        return new Attack();
    }

}
