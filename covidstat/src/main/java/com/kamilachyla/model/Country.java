package com.kamilachyla.model;

import java.util.function.Supplier;

public record Country(String name, String slug, String iso2){
    public static final Country UNKNOWN = new Country("UNKNOWN", "UNKNOWN", "UNKNOWN");
}