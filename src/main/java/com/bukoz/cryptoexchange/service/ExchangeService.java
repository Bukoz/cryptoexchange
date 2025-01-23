package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.model.ExchangeRequest;
import com.bukoz.cryptoexchange.model.ExchangeResponse;
import com.bukoz.cryptoexchange.util.FeeCalculator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ExchangeService {

    private final ExternalApiService externalApiService;
    private final FeeCalculator feeCalculator;
    private final CurrencyService currencyService;

    public ExchangeService(ExternalApiService externalApiService, FeeCalculator feeCalculator, CurrencyService currencyHandlerService) {
        this.externalApiService = externalApiService;
        this.feeCalculator = feeCalculator;
        this.currencyService = currencyHandlerService;
    }

    public ExchangeResponse getExchange(ExchangeRequest request) throws IOException, URISyntaxException {
        Map<String, BigDecimal> rates = externalApiService.fetchRates(
                currencyService.getCurrency(request.from()),
                request.to().stream()
                        .map(currencyService::getCurrency)
                        .map(CryptoCurrency::shortName)
                        .toList()
        ).rates();

        Map<String, ExchangeResponse.CurrencyExchangeForecast> forecasts = request.to().stream()
                .map(to -> CompletableFuture.supplyAsync(() -> {
                    BigDecimal rate = rates.getOrDefault(to.toUpperCase(), BigDecimal.ZERO);
                    BigDecimal fee = feeCalculator.calculateFee(request.amount());
                    BigDecimal result = rate.multiply(request.amount().subtract(fee));

                    return Map.entry(to, ExchangeResponse.CurrencyExchangeForecast
                            .builder()
                            .rate(rate)
                            .amount(request.amount())
                            .result(result)
                            .fee(fee)
                            .build());
                }))
                .map(CompletableFuture::join)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new ExchangeResponse(request.from(), forecasts);
    }
}
