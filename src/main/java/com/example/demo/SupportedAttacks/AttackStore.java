package com.example.demo.SupportedAttacks;

import java.util.Arrays;

import com.example.demo.StoreButler.StoreButler;
import com.example.demo.StoreButler.StoreButlerServices;

public class AttackStore {
    private static String[] SUPPORTED_ATTACKS_BY_NAME = loadSupportedNames();

    private static String[] loadSupportedNames() {
        try {
            String[] attackNames = StoreButler.getData(StoreButlerServices.ATTACKS, String[].class);
            return attackNames;
        } catch (Exception e) {
            System.out.println("___ATTACK STORE GOT NOTHING___" + e.getClass());
        }
        System.out.println("We only support the attack move struggle...");
        String[] defaultResult = { "struggle" };
        return defaultResult;
    }

    public static boolean isSupported(String name) {
        return Arrays.stream(SUPPORTED_ATTACKS_BY_NAME).anyMatch(attackName -> attackName.equals(name));
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