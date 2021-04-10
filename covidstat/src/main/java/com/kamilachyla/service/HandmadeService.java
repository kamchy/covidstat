
package com.kamilachyla.service;

import com.kamilachyla.model.Case;
import com.kamilachyla.model.Country;

import java.util.stream.Stream;
import java.time.LocalDate;

public final class HandmadeService implements CovidService {

  public Stream<Country> getCountries(){
    return Stream.of(
        new Country("Poland", "poland", "PL"),
        new Country("Germany", "germany", "DE")
        );
  }

  public Stream<Case> getCases(Country data) {
    if (data.name().equals("Poland")) {
      return Stream.of(
              new Case(LocalDate.parse("2021-04-07"), 1, 2, 3, 4),
              new Case(LocalDate.parse("2021-04-08"), 10, 20, 30, 40),
              new Case(LocalDate.parse("2021-04-09"), 20, 3, 35, 24)
              );
    } else {
      return Stream.of(
              new Case(LocalDate.parse("2021-04-07"), 100, 200, 300, 400),
              new Case(LocalDate.parse("2021-04-08"), 130, 120, 230, 445),
              new Case(LocalDate.parse("2021-04-09"), 230, 259, 220, 500)
              );
    }
  }
}

