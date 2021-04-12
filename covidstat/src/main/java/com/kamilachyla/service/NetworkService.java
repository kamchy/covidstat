package com.kamilachyla.service;

import com.kamilachyla.deserialize.Deserializer;
import com.kamilachyla.model.Case;
import com.kamilachyla.model.Country;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

public class NetworkService implements CovidService {
    public static final String URI_GET_COUNTRIES = "https://api.covid19api.com/countries";
    public static final String URI_GET_COUNTRY_DATA = "https://api.covid19api.com/total/country/";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Deserializer deser = new Deserializer();

    @Override
    public Stream<Case> getCases(Country data) {
        return getStreamFromNet(URI_GET_COUNTRY_DATA + data.slug(), Case.class);
    }

    @Override
    public Stream<Country> getCountries() {
        return getStreamFromNet(URI_GET_COUNTRIES, Country.class);
    }

    private <T> Stream<T> getStreamFromNet(String uri, Class<T> tClass) {
        try {
            HttpRequest request = buildGetRequest(uri);
            InputStream body = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream()).body();
            return deser.parseInputStream(tClass, body);
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    private HttpRequest buildGetRequest(String uri) {
        return HttpRequest.newBuilder().GET().uri(URI.create(uri)).build();
    }
}
