package com.example.demo.Searches.PokemonSearch;

import com.example.demo.SupportedAttacks.AttackStore;

public class MoveBySearch {
    public NameHolder move;
    public VersionGroupDetailBySearch[] version_group_details;

    public boolean isSupported() {
        return AttackStore.isSupported(move.name);
    }
}
