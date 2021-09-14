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
    public int accuracy;
    public StatChangeBySearch[] stat_changes;
    public MetaBySearch meta;
    public NameHolder target;
    public NameHolder generation;

    public Attack convert() {
        DamageClass damageClass = this.damage_class.name.equals("special") ? DamageClass.SPECIAL
                : this.damage_class.name.equals("status") ? DamageClass.STATUS : DamageClass.PHYSICAL;
        StatChange[] changes = Arrays.stream(this.stat_changes).map(change -> change.convert())
                .toArray(StatChange[]::new);
        Meta meta = this.meta.convert();

        this.adjustAccuracy();

        return new Attack(this.name, this.power, damageClass, this.type.convert(), this.accuracy, changes, meta,
                this.enemyIsTarget());
    }

    private void adjustAccuracy() {
        // Due to some errors in the pokemon api, this step is necessary. F.e., "swift"
        // (="Sternenschauer") has accuracy null
        if (this.accuracy <= 0 || this.accuracy > 100) {
            this.accuracy = 100;
        }
    }

    private boolean enemyIsTarget() {
        switch (this.target.name) {
            case "users-field":
                return false;
            case "all-other-pokemon":
                return true;
            case "random-opponent":
                return true;
            case "user":
                return false;
            case "opponents-field":
                return true;
            case "selected-pokemon":
                return true;
            case "all-opponents":
                return true;
            case "specific-move":
                return true;

            default:
                try {
                    throw new Exception("InvalidTargetKey");
                } catch (Exception e) {
                    System.out.println("___TARGET KEY OF " + this.name + "CAN NOT BE HANDLED___");
                }
                return true;
        }
    }
}