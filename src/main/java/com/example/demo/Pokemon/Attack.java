package com.example.demo.Pokemon;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Translater.Translater;

public class Attack {
    private final static Type POKE_TYPE_NORMAL = new Type("normal", "https://pokeapi.co/api/v2/type/1/");
    private String name;
    private int power;
    private DamageClass damageClass;
    private Type type;
    private int accurancy;
    private StatChange[] statChanges;
    private Meta meta;
    private boolean enemyIsTarget;

    public Attack() {
        this.name = "struggle";
        this.power = 50;
        this.type = POKE_TYPE_NORMAL;
        this.damageClass = DamageClass.PHYSICAL;
        this.accurancy = 100;
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

    public Attack(String name, int power, DamageClass damageClass, Type type, int accurancy, StatChange[] statChanges,
            Meta meta, boolean enemyIsTarget) {
        Type classicalType = type.name.toUpperCase().equals("DARK") ? POKE_TYPE_NORMAL : type;
        this.name = name;
        this.power = power;
        this.damageClass = damageClass;
        this.type = classicalType;
        this.accurancy = accurancy;
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
