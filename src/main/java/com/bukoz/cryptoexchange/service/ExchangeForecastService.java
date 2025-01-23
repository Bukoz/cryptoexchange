package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.model.ExchangeRequest;
import com.bukoz.cryptoexchange.model.ExchangeResponse;
import com.bukoz.cryptoexchange.util.FeeCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ExchangeForecastService {

    private final FeeCalculator feeCalculator;

    public ExchangeForecastService(FeeCalculator feeCalculator) {
        this.feeCalculator = feeCalculator;
    }

    public Map<String, ExchangeResponse.CurrencyExchangeForecast> getCurrencyExchangeForecasts(ExchangeRequest request, Map<String, BigDecimal> rates) {
        List<Map.Entry<String, Integer>> indexedTargets = IntStream.range(0, request.to().size())
                .mapToObj(index -> Map.entry(request.to().get(index), index))
                .toList(); // fix to provide correct order, as using ComparableFuture on its own was not providing same results every time

        List<CompletableFuture<Map.Entry<Integer, Map.Entry<String, ExchangeResponse.CurrencyExchangeForecast>>>> futureList =
                indexedTargets.stream()
                        .map(indexedTarget -> CompletableFuture.supplyAsync(() -> {
                            String to = indexedTarget.getKey();
                            int index = indexedTarget.getValue();

                            BigDecimal rate = rates.getOrDefault(to.toUpperCase(), BigDecimal.ZERO);
                            BigDecimal fee = feeCalculator.calculateFee(request.amount());
                            BigDecimal result = rate.multiply(request.amount().subtract(fee));

                            return Map.entry(index, Map.entry(to, ExchangeResponse.CurrencyExchangeForecast
                                    .builder()
                                    .rate(rate)
                                    .amount(request.amount())
                                    .result(result)
                                    .fee(fee)
                                    .build()));
                        }))
                        .toList();

        return futureList.stream()
                .map(CompletableFuture::join)
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a, // handle duplicate keys
                        LinkedHashMap::new
                ));
    }
}
