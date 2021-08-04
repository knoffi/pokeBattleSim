package com.example.demo.Pokemon;

import com.example.demo.Searches.MoveSearch.MetaBySearch;

public class Meta {
    public int heal;
    public int drain;
    public int maxHits;
    public int minHits;
    public int maxTurns;
    public int minTurns;
    public int critRate;
    public int flinchChance;
    public int statusChance;
    public int ailmentChance;
    public String ailment;
    public String category;

    public Meta(MetaBySearch meta) {
        this.heal = meta.healing;
        this.drain = meta.drain;
        this.ailment = meta.ailment.name;
        this.ailmentChance = meta.ailment_chance;
        this.statusChance = meta.stat_chance;
        this.category = meta.category.name;
        this.critRate = meta.crit_rate;
        this.flinchChance = meta.flinch_chance;
        this.maxHits = meta.max_hits;
        this.minHits = meta.min_hits;
        this.minTurns = meta.min_turns;
        this.maxTurns = meta.max_turns;
    }

    public Meta() {
        this.ailment = "none";
        this.category = "damage";

    }
}
