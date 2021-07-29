package com.example.demo.SupportedAttacks;

import java.util.Arrays;

public class AttackStore {
    static void update() {

    }
}

class SupportedMoveCategories {
    private static String[] categories = { "damage", "damage+lower", "damage+raise", "net-good-stats" };

    public static boolean contains(String testCategory) {
        var testIsSupported = Arrays.stream(categories).anyMatch(category -> category.equals(testCategory));
        return testIsSupported;
    };
}
