package com.example.demo.SupportedAttacks;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import com.example.demo.RequestMode;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Pokemon.Attack;
import com.example.demo.Searches.MoveSearch.MoveNameHolders;
import com.example.demo.Searches.MoveSearch.MoveSearch;
import com.example.demo.Searches.PokemonSearch.NameHolder;

public class AttackStore {
    private static Attack getAttackFromUrl(String url) {
        try {
            String path = Pokedex.getPathFromURL(url);
            Attack attack = Pokedex.getPokeData(path, MoveSearch.class, RequestMode.JAVA_11).convert();
            return attack;
        } catch (IOException | InterruptedException | NullPointerException e) {
            System.out.println("___ERROR IN GETTING ATTACK FROM URL___" + e.getClass());
        }
        return new Attack();
    }

    private static Attack[] getClassicalAttacks() {
        try {
            NameHolder[] nameHolders = Pokedex.getPokeData(Pokedex.API_PATH + Pokedex.GENERATION_I_PATH,
                    MoveNameHolders.class, RequestMode.JAVA_11).moves;
            /*NameHolder[] trimmedNameHolders = { nameHolders[0], nameHolders[1], nameHolders[2], nameHolders[3],
                    nameHolders[4], nameHolders[5], nameHolders[6], nameHolders[7], nameHolders[8], nameHolders[9],
                    nameHolders[10] };*/
            Attack[] attacks = Arrays.stream(nameHolders).map(nameHolder -> getAttackFromUrl(nameHolder.url))
                    .toArray(Attack[]::new);
            return attacks;
        } catch (IOException | InterruptedException | NullPointerException e) {
            System.out.println("___PROBLEM AT LOADING ATTACKS___" + e.getClass());
        }

        return new Attack[0];
    }

    private static Attack[] getSupportedAttacks(Attack[] attacks) {
        return Arrays.stream(attacks).filter(attack -> SupportedMoveCategories.contain(attack.getCategory()))
                .toArray(Attack[]::new);
    }

    public static void update() throws IOException {
        FileWriter fileWriter = new FileWriter(
                "C:\\Users\\monop\\programming\\pokeFightApi\\pokeBattleSim\\src\\main\\java\\com\\example\\demo\\SupportedAttacks\\test.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        String supportedAttackNames = "here we got \n";
        Attack[] classicalAttacks = getClassicalAttacks();
        Attack[] supportedAttacks = getSupportedAttacks(classicalAttacks);
        for (int i = 0; i < supportedAttacks.length; i++) {
            supportedAttackNames += supportedAttacks[i].getName() + "\n";
        }
        printWriter.print(supportedAttackNames);
        printWriter.close();
    }
}

class SupportedMoveCategories {
    private static String[] categories = { "damage", "damage+ailment", "damage+lower", "damage+raise",
            "net-good-stats" };

    public static boolean contain(String testCategory) {
        boolean testIsSupported = Arrays.stream(categories).anyMatch(category -> category.equals(testCategory));
        return testIsSupported;
    };
}
