package com.example.demo.Pokemon;

public class Stat {
    static final int MAX_MODIFIER = 6;
    static final int MIN_MODIFIER = -6;
    static final int MAX_VALUE = 999;
    static final int MIN_VALUE = 1;
    String name;
    private int value;
    private int modifier;

    public Stat(String name, int value) {
        this.name = name;
        this.value = value;
        this.modifier = 0;
    }

    public int getValue() {
        int productWithoutBoundCheck = (int) (this.value * this.getModifierFactor());
        return adjustToBounds(productWithoutBoundCheck, false);

    }

    public void changeModifier(int change) {
        int sumWithoutBoundCheck = this.modifier + change;
        this.modifier = adjustToBounds(sumWithoutBoundCheck, true);

    }

    private static int adjustToBounds(int newNumber, boolean isForModifier) {
        if (isForModifier) {
            int adjustedToLowerBound = Math.max(MIN_MODIFIER, newNumber);
            int adjustedCompletely = Math.min(MAX_MODIFIER, adjustedToLowerBound);
            return adjustedCompletely;
        } else {
            int adjustedToLowerBound = Math.max(MIN_VALUE, newNumber);
            int adjustedCompletely = Math.min(MAX_VALUE, adjustedToLowerBound);
            return adjustedCompletely;
        }
    }

    private double getModifierFactor() {
        switch (this.modifier) {
            case 6:

                return 8.0 / 2;
            case 5:

                return 7.0 / 2;
            case 4:

                return 6.0 / 2;
            case 3:

                return 5.0 / 2;
            case 2:

                return 4.0 / 2;
            case 1:

                return 3.0 / 2;
            case 0:

                return 2.0 / 2;
            case -1:

                return 2.0 / 3;
            case -2:

                return 2.0 / 4;
            case -3:

                return 2.0 / 5;
            case -4:

                return 2.0 / 6;
            case -5:

                return 2.0 / 7;
            case -6:

                return 2.0 / 8;

            default:
                try {
                    throw new Exception("StatModifierOutOfBound");
                } catch (Exception e) {
                    System.out.println("___Stat modifier was out of bounce___" + this.name + " " + this.modifier);
                }
                return 1;
        }
    }
}