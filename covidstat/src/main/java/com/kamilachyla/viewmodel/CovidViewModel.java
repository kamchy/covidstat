package com.kamilachyla.viewmodel;

import com.kamilachyla.model.Case;
import com.kamilachyla.model.CasesPerCountryModel;
import com.kamilachyla.model.Country;
import com.kamilachyla.service.CovidService;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CovidViewModel {
    private final SimpleListProperty<Country> countries = new SimpleListProperty<>(FXCollections.emptyObservableList());
    private final SimpleObjectProperty<Country> selectedCountry = new SimpleObjectProperty<>(Country.UNKNOWN);

    private final SimpleListProperty<Case> selectedCountryCases = new SimpleListProperty<>();
    private final SimpleObjectProperty<Case> currentCase = new SimpleObjectProperty<>(Case.EMPTY);

    private final SimpleIntegerProperty confirmed = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty deaths = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty active = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty recovered = new SimpleIntegerProperty(0);
    private final SimpleObjectProperty<LocalDate> localDateProperty = new SimpleObjectProperty<>(LocalDate.MIN);


    private final CountriesProviderService countriesService;
    private final CovidUpdateService casesService;
    private final CasesPerCountryModel model;

    public SimpleObjectProperty<Country> selectedCountryProperty() {
        return selectedCountry;
    }

    public SimpleObjectProperty<Case> currentCaseProperty() {
        return currentCase;
    }

    public SimpleIntegerProperty confirmedProperty() {
            return confirmed;
        }

        public SimpleIntegerProperty deathsProperty() {
            return deaths;
        }

        public SimpleIntegerProperty activeProperty() {
            return active;
        }

        public SimpleIntegerProperty recoveredProperty() {
            return recovered;
        }

    public CovidViewModel(CovidService downloader) {
        this.model = new CasesPerCountryModel();

        this.countriesService = new CountriesProviderService(downloader);
        countriesService.setOnSucceeded(wse -> {
            model.setCountries(countriesService.getValue());
            countries.set(FXCollections.observableList(model.getCountries()));
        });

        this.casesService = new CovidUpdateService(downloader);
        countriesService.start();
        casesService.setOnSucceeded(wse -> {
            final Country sel = selectedCountry.get();
            if (sel != null) {
                model.addCases(sel, casesService.getValue());
                selectedCountryCases.set(FXCollections.observableList(model.getCases(sel)));
                System.out.printf(" with case %s%n", currentCase.get());

            }
        });

        selectedCountry.addListener((o, ov, nv) -> {
            if (model.getCases(nv).isEmpty()) {
                casesService.restart();
            }
            selectedCountryCases.set(FXCollections.observableList(model.getCases(nv)));
        });

        // currentCase will always point to 0th element of selectedCountryCases when it is changed
        selectedCountryCases.addListener((o, ov, nv) -> {
            currentCase.set(nv.isEmpty() ? Case.EMPTY : nv.get(nv.size() - 1));
        });

        // statistics fields will react to currentCase changes
        currentCase.addListener((c, ov, nv) ->{
            active.set(nv.active());
            deaths.set(nv.deaths());
            recovered.set(nv.recovered());
            confirmed.set(nv.confirmed());
            localDateProperty.set(nv.date());

        });
    }

    public SimpleListProperty<Country> countriesProperty() {
        return countries;
    }

    public void update(Country nv) {
        selectedCountry.set(nv);
    }

    public Property<LocalDate> getDateProperty() {
        return localDateProperty;
    }

        public static class CountriesProviderService extends Service<List<Country>> {
            private final CovidService service;

            public CountriesProviderService(CovidService service) {
                this.service = service;
            }
            @Override
            protected Task<List<Country>> createTask() {
                Task<List<Country>>  t = new Task<>() {
                    @Override
                    protected List<Country> call() throws Exception {
                        return service.getCountries()
                                .sorted(Comparator.comparing(Country::name))
                                .collect(Collectors.toList());

                    }
                };
                return t;
            }
    }

    public class CovidUpdateService extends Service<List<Case>> {
        private final CovidService service;

        public CovidUpdateService(CovidService service) {
            this.service = service;
        }

        @Override
        protected Task<List<Case>> createTask() {
            return new Task<>() {

                @Override
                protected List<Case> call() {
                    Country country = selectedCountry.get();
                    List<Case> cases = List.of();
                    if (country != null) {
                        cases = service.getCases(country)
                                .sorted(Comparator.comparing(Case::date))
                                .collect(Collectors.toList());
                    }
                    return cases;
                }
            };
        }
    }
}
