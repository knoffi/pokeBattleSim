package com.example.demo.TypeEffects;

import java.util.Arrays;

import com.example.demo.Searches.PokemonSearch.NameHolder;

public class DamageRelationsBySearch {
    public NameHolder[] double_damage_from;
    public NameHolder[] double_damage_to;
    public NameHolder[] half_damage_from;
    public NameHolder[] half_damage_to;
    public NameHolder[] no_damage_from;
    public NameHolder[] no_damage_to;

    public String[] getDamageNames(int relationIndex) {
        NameHolder[] damageHolders = this.getDamageHolder(relationIndex);
        return Arrays.stream(damageHolders).map(nameHolder -> nameHolder.name).toArray(String[]::new);
    }

    private NameHolder[] getDamageHolder(int relationIndex) {
        switch (relationIndex) {
            case 0:
                return this.double_damage_from;

            case 1:
                return this.double_damage_to;

            case 2:
                return this.half_damage_from;

            case 3:
                return this.half_damage_to;

            case 4:
                return this.no_damage_from;

            default:
                return this.no_damage_to;

        }
    }
}