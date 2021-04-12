package com.kamilachyla.service;

import com.kamilachyla.deserialize.Deserializer;
import com.kamilachyla.model.Case;
import com.kamilachyla.model.Country;

import java.io.InputStream;
import java.util.Objects;
import java.util.stream.Stream;

public class FileService implements CovidService {
    private static final String COUNTRIES_FILE = "countries.json";
    private final Deserializer deserializer = new Deserializer();

    public FileService() {

    }
    @Override
    public Stream<Country> getCountries() {
        return deserialize(COUNTRIES_FILE, Country.class);
    }

    private <T> Stream<T> deserialize(String fname, Class<T> tClass) {
        Stream<T> result;
        try(InputStream is = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fname))){
            result = deserializer.parseInputStream(tClass, is);
        } catch (Exception e) {
            result = Stream.empty();
        }

        return result;
    }

    @Override
    public Stream<Case> getCases(Country data) {
        return deserialize(data.slug() + ".json", Case.class);
    }
}
