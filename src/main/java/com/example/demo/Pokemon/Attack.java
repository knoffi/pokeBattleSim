package com.example.demo.Pokemon;

public class Attack {
    private static Type POKE_TYPE_NORMAL = new Type("normal", "https://pokeapi.co/api/v2/type/1/");
    private String name;
    private int power;
    private DamageClass damageClass;
    private Type type;
    private int accurancy;
    private StatChange[] statChanges;
    private Meta meta;

    public Attack() {
        this.name = "struggle";
        this.power = 50;
        this.type = POKE_TYPE_NORMAL;
        this.damageClass = DamageClass.PHYSICAL;
        this.accurancy = 100;
        this.statChanges = new StatChange[0];
        this.meta = new Meta();
    }

    public boolean doesDamage() {
        return this.power > 0 || this.meta.category.substring(0, 3).equals("dam");
    }

    public Attack(String name, int power, DamageClass damageClass, Type type, int accurancy, StatChange[] statChanges,
            Meta meta) {
        Type classicalType = type.name.toUpperCase().equals("DARK") ? POKE_TYPE_NORMAL : type;
        this.name = name;
        this.power = power;
        this.damageClass = damageClass;
        this.type = classicalType;
        this.accurancy = accurancy;
        this.statChanges = statChanges;
        this.meta = meta;
    }

    public Type getType() {
        return this.type.name.toUpperCase().equals("DARK") ? POKE_TYPE_NORMAL : type;
    }

    public String getName() {
        return this.name;
    }

    public String getCategory() {
        return this.type.name.toUpperCase().equals("DARK") ? "physical" : this.meta.category;
    }
}
