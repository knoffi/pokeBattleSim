package com.example.demo.Pokemon;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Translater.Translater;

public class Attack {
    private final static Type POKE_TYPE_NORMAL = new Type("normal", "https://pokeapi.co/api/v2/type/1/");
    private String name;
    private int power;
    private DamageClass damageClass;
    private Type type;
    private int accuracy;
    private StatChange[] statChanges;
    private Meta meta;
    private boolean enemyIsTarget;

    public Attack() {
        this.name = "struggle";
        this.power = 50;
        this.type = POKE_TYPE_NORMAL;
        this.damageClass = DamageClass.PHYSICAL;
        this.accuracy = 100;
        this.statChanges = new StatChange[0];
        this.meta = new Meta();
        this.enemyIsTarget = true;
    }

    public boolean doesDamage() {
        return this.power > 0 || this.meta.category.substring(0, 3).equals("dam");
    }

    public boolean isPureStatChanger() {
        return this.meta.category.substring(0, 3).equals("net");
    }

    public StatChange[] getStatChanges() {
        return this.statChanges;
    }

    public boolean enemyIsTarget() {
        return this.enemyIsTarget;
    }

    public double getAccuracy(int attackerAccuracy, int defenderEvasion) {
        try {
            double accuracyFactor = getHitFactor(attackerAccuracy);
            try {
                double evasionFactor = 1.0 / getHitFactor(defenderEvasion);
                return this.accuracy / 100.0 * accuracyFactor * evasionFactor;
            } catch (Exception e2) {
                System.out.println("___EVASION MODIFIER OUT OF BOUNDARY___");
            }
        } catch (Exception e1) {
            System.out.println("___ACCURANCY MODIFIER OUT OF BOUNDARY___");
        }
        return 1.0;

    }

    private static double getHitFactor(int value) throws Exception {
        switch (value) {
            case -6:
                return 0.25;
            case -5:
                return 0.28;
            case -4:
                return 0.33;
            case -3:
                return 0.4;
            case -2:
                return 0.5;
            case -1:
                return 0.66;
            case 0:
                return 1.0;
            case 1:
                return 1.5;
            case 2:
                return 2.0;
            case 3:
                return 2.5;
            case 4:
                return 3.0;
            case 5:
                return 3.5;
            case 6:
                return 4.0;

            default:
                throw new Exception("HitValueOutOfBounce");

        }
    }

    public Attack(String name, int power, DamageClass damageClass, Type type, int accuracy, StatChange[] statChanges,
            Meta meta, boolean enemyIsTarget) {
        Type classicalType = type.name.toUpperCase().equals("DARK") ? POKE_TYPE_NORMAL : type;
        this.name = name;
        this.power = power;
        this.damageClass = damageClass;
        this.type = classicalType;
        this.accuracy = accuracy;
        this.statChanges = statChanges;
        this.meta = meta;
        this.enemyIsTarget = enemyIsTarget;
    }

    public void translateName(Languages language) {
        this.name = Translater.getTranslatedAttack(this.name, language);
    }

    public Type getType() {
        return this.type.name.toUpperCase().equals("DARK") ? POKE_TYPE_NORMAL : type;
    }

    public String getName() {
        return this.name;
    }

    public DamageClass getDamageClass() {
        return this.type.name.toUpperCase().equals("DARK") ? DamageClass.PHYSICAL : this.damageClass;
    }

    public int getPower() {
        return this.power;
    }
}
