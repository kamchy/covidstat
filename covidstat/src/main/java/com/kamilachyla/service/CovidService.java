package com.kamilachyla.service;

import com.kamilachyla.model.Case;
import com.kamilachyla.model.Country;

import java.util.stream.Stream;

public interface CovidService {

  Stream<Country> getCountries();
  Stream<Case> getCases(Country data);
}




