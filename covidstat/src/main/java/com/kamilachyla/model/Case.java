package com.kamilachyla.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record Case(
        @JsonProperty("Date") LocalDate date,
        @JsonProperty("Confirmed") int confirmed,
        @JsonProperty("Deaths") int deaths,
        @JsonProperty("Recovered") int recovered,
        @JsonProperty("Active") int active){
    public static final Case EMPTY = new Case(LocalDate.EPOCH, 0, 0, 0, 0);
}