package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.externalapi.coingecko.CoinGeckoCurrencyRateApiClient;
import com.bukoz.cryptoexchange.model.CurrencyRateResponse;
import com.bukoz.cryptoexchange.util.RateProcessor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class CoinGeckoApiService implements ExternalApiService {

    private final CoinGeckoCurrencyRateApiClient apiClient;
    private final RateProcessor rateProcessor;

    public CoinGeckoApiService(CoinGeckoCurrencyRateApiClient apiClient, RateProcessor rateProcessor) {
        this.apiClient = apiClient;
        this.rateProcessor = rateProcessor;
    }

    public CurrencyRateResponse fetchRates(CryptoCurrency currency, List<String> filters) {
        Map<String, Object> response = apiClient.fetchRates(currency, filters);

        @SuppressWarnings("unchecked")
        Map<String, Object> rawRates = (Map<String, Object>) response.get(currency.longName());
        Map<String, BigDecimal> rates = rateProcessor.processRates(rawRates);

        return new CurrencyRateResponse(currency.shortName(), rates);
    }
}

