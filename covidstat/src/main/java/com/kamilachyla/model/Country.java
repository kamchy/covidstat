package com.kamilachyla.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Country(
        @JsonProperty("Country") String name,
        @JsonProperty("Slug") String slug,
        @JsonProperty("ISO2") String iso2) {
    public static final Country UNKNOWN = new Country("UNKNOWN", "UNKNOWN", "UNKNOWN");

}