package com.example.demo.Searches.PokemonSearch;

import java.util.Arrays;

public class MoveBySearch {
    public NameHolder move;
    public VersionGroupDetailBySearch[] version_group_details;

    public boolean isClassical() {
        return Arrays.stream(this.version_group_details).anyMatch(VersionGroupDetailBySearch::isClassical);
    }
}
