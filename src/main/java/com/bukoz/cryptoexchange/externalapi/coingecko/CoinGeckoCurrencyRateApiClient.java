package com.bukoz.cryptoexchange.externalapi.coingecko;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.exception.external.ExternalApiException;
import com.bukoz.cryptoexchange.externalapi.common.CurrencyRateApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CoinGeckoCurrencyRateApiClient implements CurrencyRateApiClient {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public CoinGeckoCurrencyRateApiClient(RestTemplate restTemplate, @Value("${external.api.coingecko.url.price}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    public Map<String, Object> fetchRates(CryptoCurrency currency, List<String> filters) {
        String url = CoinGeckoPriceApiHelper.buildPriceApiUrl(currency.longName(), filters, apiUrl);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response == null || !response.containsKey(currency.longName())) {
            log.debug("External API returned an empty response for the URL {}", url);
            throw new ExternalApiException("Received empty response from external API");
        }

        return response;
    }

}
