package com.bukoz.cryptoexchange.externalapi.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

@Component
public class SupportedCurrenciesFetcher {

    @Value("${external.api.url.supported-currencies}")
    private String supportedCurrenciesApiUrl;

    public List<String> fetchSupportedCurrencies() throws IOException, URISyntaxException {
        URI uri = new URI(supportedCurrenciesApiUrl);
        URL url = uri.toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        StringBuilder responseBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            while (scanner.hasNext()) {
                responseBuilder.append(scanner.nextLine());
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBuilder.toString(), new TypeReference<>() {
        });
    }
}
