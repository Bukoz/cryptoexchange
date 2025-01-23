package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.model.ExchangeRequest;
import com.bukoz.cryptoexchange.model.ExchangeResponse;
import com.bukoz.cryptoexchange.util.FeeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeForecastServiceTest {

    private static final String BTC = "BTC";
    private static final String XRP = "XRP";
    private static final String ETH = "ETH";
    private ExchangeForecastService exchangeForecastService;
    private Map<String, ExchangeResponse.CurrencyExchangeForecast> expectedForecasts;

    @Mock
    private FeeCalculator feeCalculator;

    @BeforeEach
    void setUp() {
        exchangeForecastService = new ExchangeForecastService(feeCalculator);
        expectedForecasts = new LinkedHashMap<>();
        expectedForecasts.put(XRP, ExchangeResponse.CurrencyExchangeForecast.builder()
                .rate(BigDecimal.valueOf(0.9))
                .amount(BigDecimal.valueOf(100))
                .fee(BigDecimal.valueOf(5.00))
                .result(BigDecimal.valueOf(85.5)) // (100 - 5) * 0.9
                .build());
        expectedForecasts.put(ETH, ExchangeResponse.CurrencyExchangeForecast.builder()
                .rate(BigDecimal.valueOf(110.0))
                .amount(BigDecimal.valueOf(100))
                .fee(BigDecimal.valueOf(5.00))
                .result(BigDecimal.valueOf(10450)) // (100 - 5) * 110
                .build());
    }

    @Test
    void getCurrencyExchangeForecasts() {
        when(feeCalculator.calculateFee(any(BigDecimal.class))).thenReturn(new BigDecimal("5.00"));
        ExchangeRequest request = new ExchangeRequest(
                BTC,
                List.of(XRP, ETH),
                BigDecimal.valueOf(100)
        );
        Map<String, BigDecimal> rates = Map.of(
                XRP, BigDecimal.valueOf(0.9),
                ETH, BigDecimal.valueOf(110.0)
        );

        Map<String, ExchangeResponse.CurrencyExchangeForecast> result =
                exchangeForecastService.getCurrencyExchangeForecasts(request, rates);

        assertEquals(expectedForecasts.size(), result.size());
    }

    @Test
    void getCurrencyExchangeForecastsWhileSameCurrencyIsCalledTwice() {
        when(feeCalculator.calculateFee(any(BigDecimal.class))).thenReturn(new BigDecimal("5.00"));
        ExchangeRequest request = new ExchangeRequest(
                BTC,
                List.of(XRP, ETH, XRP),
                BigDecimal.valueOf(100)
        );
        Map<String, BigDecimal> rates = Map.of(
                XRP, BigDecimal.valueOf(0.9),
                ETH, BigDecimal.valueOf(110.0)
        );

        Map<String, ExchangeResponse.CurrencyExchangeForecast> result =
                exchangeForecastService.getCurrencyExchangeForecasts(request, rates);

        assertEquals(expectedForecasts.size(), result.size());
    }
}
