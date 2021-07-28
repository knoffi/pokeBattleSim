package com.example.demo.Pokemon;

public class Attack {
    String name;
    String text;
    int damage;
    DamageClass damageClass;
    Type type;
    int accurancy;
    StatChange[] statChanges;
}

enum DamageClass {
    PHYSICAL, SPECIAL
}
