package com.example.demo.Searches.PokemonSearch;

import com.example.demo.Pokemon.Type;

public class TypeHolder {
    public NameHolder type;

    TypeHolder() {
        super();
    }

    public Type convert() {
        return new Type(this.type.name, this.type.url);
    }
}
