package com.kamilachyla.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.kamilachyla.model.Case;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CaseDeserializer extends StdDeserializer<Case> {

    public CaseDeserializer() {
        this(null);
    }

    public CaseDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Case deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        int confirmed = node.get("Confirmed").intValue();
        int deaths = node.get("Deaths").intValue();
        int recovered = node.get("Recovered").intValue();
        int active = node.get("Active").intValue();
        String dateStr = node.get("Date").asText();
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
        return new Case(date, confirmed, deaths, recovered, active);
    }
}