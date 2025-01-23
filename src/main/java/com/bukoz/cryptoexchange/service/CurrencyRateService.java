package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.model.CurrencyRateResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class CurrencyRateService {

    private final ExternalApiService externalApiService;

    public CurrencyRateService(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    public CurrencyRateResponse getCurrencyRates(CryptoCurrency baseCurrency, List<CryptoCurrency> filters) throws IOException, URISyntaxException {
        if (filters.isEmpty()) {
            throw new IllegalArgumentException("Filter list should not be empty");
        }
        List<String> filterShortNames = filters.stream().map(CryptoCurrency::shortName).toList();
        return externalApiService.fetchRates(baseCurrency, filterShortNames);
    }
}
