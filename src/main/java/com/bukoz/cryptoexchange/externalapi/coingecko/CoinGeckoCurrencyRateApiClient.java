package com.bukoz.cryptoexchange.externalapi.coingecko;

import com.bukoz.cryptoexchange.exception.external.ExternalApiException;
import com.bukoz.cryptoexchange.externalapi.CurrencyRateApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CoinGeckoCurrencyRateApiClient implements CurrencyRateApiClient {

    @Value("${external.api.url.price}")
    private String priceApiUrl;

    private final RestTemplate restTemplate;

    public CoinGeckoCurrencyRateApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> fetchRates(String currency, List<String> filters) {
        String url = buildPriceApiUrl(currency, filters);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response == null || !response.containsKey(currency)) {
            log.warn("External API returned an empty response for the URL {}", url);
            throw new ExternalApiException("Received empty response from external API");
        }

        return response;
    }

    private String buildPriceApiUrl(String currency, List<String> filters) {
        String filterString = String.join(",", filters);
        return String.format("%s?ids=%s&vs_currencies=%s", priceApiUrl, currency, filterString);
    }

}

