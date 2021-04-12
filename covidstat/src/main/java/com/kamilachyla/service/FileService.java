package com.kamilachyla.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kamilachyla.deserialize.CaseDeserializer;
import com.kamilachyla.model.Case;
import com.kamilachyla.model.Country;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class FileService implements CovidService {
    private static final String COUNTRIES_FILE = "countries.json";
    private final ObjectMapper mapper = new ObjectMapper();

    public FileService() {
        mapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Case.class, new CaseDeserializer());
        mapper.registerModule(module);
    }
    @Override
    public Stream<Country> getCountries() {
        return deserialize(COUNTRIES_FILE, Country.class);
    }

    private <T> Stream<T> deserialize(String fname, Class<T> tClass) {
        Stream<T> result;
        try(InputStream is = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fname))){
            List<T> values = mapper.readerForListOf(tClass).readValue(is);
            result = values.stream();
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
