package com.example.demo.Searches.MoveSearch;

import java.util.Arrays;

import com.example.demo.Pokemon.Attack;
import com.example.demo.Pokemon.DamageClass;
import com.example.demo.Pokemon.Meta;
import com.example.demo.Pokemon.StatChange;
import com.example.demo.Searches.PokemonSearch.NameHolder;

public class MoveSearch {
    public String name;
    public int power;
    public NameHolder damage_class;
    public NameHolder type;
    public int accurancy;
    public StatChangeBySearch[] stat_changes;
    public MetaBySearch meta;

    public Attack convert() {
        DamageClass damageClass = this.damage_class.name.equals("special") ? DamageClass.SPECIAL
                : this.damage_class.name.equals("status") ? DamageClass.STATUS : DamageClass.PHYSICAL;
        StatChange[] changes = Arrays.stream(this.stat_changes).map(change -> change.convert())
                .toArray(StatChange[]::new);
        Meta meta = this.meta.convert();
        return new Attack(this.name, this.power, damageClass, this.type.convert(), this.accurancy, changes, meta);
    }
}