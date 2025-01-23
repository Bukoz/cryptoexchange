package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.model.ExchangeRequest;
import com.bukoz.cryptoexchange.model.ExchangeResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class ExchangeService {

    private final ExternalApiService externalApiService;
    private final CurrencyService currencyService;
    private final ExchangeForecastService exchangeForecastService;

    public ExchangeService(ExternalApiService externalApiService, CurrencyService currencyService, ExchangeForecastService exchangeForecastService) {
        this.externalApiService = externalApiService;
        this.currencyService = currencyService;
        this.exchangeForecastService = exchangeForecastService;
    }

    public ExchangeResponse getExchange(ExchangeRequest request) throws IOException, URISyntaxException {
        Map<String, BigDecimal> rates = externalApiService.fetchRates(
                currencyService.getCurrency(request.from()),
                request.to().stream()
                        .map(currencyService::getCurrency)
                        .map(CryptoCurrency::shortName)
                        .toList()
        ).rates();

        Map<String, ExchangeResponse.CurrencyExchangeForecast> forecasts = exchangeForecastService.getCurrencyExchangeForecasts(request, rates);

        return new ExchangeResponse(request.from(), forecasts, "Each forecast is calculated after subtracting fee from original currency (" + request.from() + ")");
    }

}
