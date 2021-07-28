package com.example.demo.Searches.PokemonSearch;

import com.example.demo.Pokemon.Type;

public class NameHolder {
    public String name;
    public String url;

    public Type convert() {
        return new Type(this.name, this.url);
    }

}