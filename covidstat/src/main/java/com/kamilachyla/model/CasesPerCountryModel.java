package com.kamilachyla.model;

import java.util.*;

public class CasesPerCountryModel {
    private final Map<Country, List<Case>> countryCasesMap = new HashMap<>();
    private final List<Country> countries = new ArrayList<>();

    public List<Case> getCases(Country country) {
        return countryCasesMap.getOrDefault(country, Collections.emptyList());
    }

    public void addCases(Country country, List<Case> cases) {
        countryCasesMap.put(country, cases);
    }

    public void setCountries(List<Country> countries) {
        this.countries.clear();
        this.countries.addAll(countries);
    }

    public List<Country> getCountries() {
        return List.copyOf(countries);
    }
}
