package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.model.CurrencyRate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class CurrencyService {

    private final ExternalApiService externalApiService;

    public CurrencyService(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }


    public CurrencyRate getCurrencyRates(String source, List<String> filters) throws IOException, URISyntaxException {
        return externalApiService.fetchRates(source, filters);
    }

}
