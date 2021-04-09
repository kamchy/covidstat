package com.kamilachyla.model;

import java.time.LocalDate;

public record Case(LocalDate date, int confirmed, int deaths, int recovered, int active){
    public static final Case EMPTY = new Case(LocalDate.EPOCH, 0, 0, 0, 0);
}