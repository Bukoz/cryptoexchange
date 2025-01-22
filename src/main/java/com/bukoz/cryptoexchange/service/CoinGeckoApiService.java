package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.externalapi.coingecko.CoinGeckoCurrencyRateApiClient;
import com.bukoz.cryptoexchange.externalapi.coingecko.CoinGeckoSupportedCurrenciesApiClient;
import com.bukoz.cryptoexchange.model.CurrencyRate;
import com.bukoz.cryptoexchange.util.RateProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CoinGeckoApiService implements ExternalApiService {

    private final CoinGeckoCurrencyRateApiClient apiClient;
    private final CoinGeckoSupportedCurrenciesApiClient fetcher;
    private final RateProcessor rateProcessor;

    public CoinGeckoApiService(CoinGeckoCurrencyRateApiClient apiClient, CoinGeckoSupportedCurrenciesApiClient fetcher, RateProcessor rateProcessor) {
        this.apiClient = apiClient;
        this.fetcher = fetcher;
        this.rateProcessor = rateProcessor;
    }

    public CurrencyRate fetchRates(String currency, List<String> filters) throws IOException, URISyntaxException {
        List<String> currencies = (filters == null || filters.isEmpty())
                ? fetcher.fetchSupportedCurrencies()
                : filters;

        Map<String, Object> response = apiClient.fetchRates(currency, currencies);

        @SuppressWarnings("unchecked")
        Map<String, Object> rawRates = (Map<String, Object>) response.get(currency);
        Map<String, BigDecimal> rates = rateProcessor.processRates(rawRates);

        return new CurrencyRate(currency, rates);
    }


}
