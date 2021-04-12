package com.kamilachyla.deserialize;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kamilachyla.model.Case;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

public class Deserializer {
    private final ObjectMapper mapper = new ObjectMapper();
    public Deserializer() {
        mapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Case.class, new CaseDeserializer());
        mapper.registerModule(module);
    }

    public <T> Stream<T> parseInputStream(Class<T> tClass, InputStream is) throws java.io.IOException {
        List<T> values = mapper.readerForListOf(tClass).readValue(is);
        is.close();
        return values.stream();
    }
}
