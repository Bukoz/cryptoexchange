package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.externalapi.coingecko.CoinGeckoCurrencyRateApiClient;
import com.bukoz.cryptoexchange.model.CurrencyRateResponse;
import com.bukoz.cryptoexchange.util.RateProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoinGeckoApiServiceTest {

    @Mock
    private CoinGeckoCurrencyRateApiClient apiClient;
    @Mock
    private RateProcessor rateProcessor;

    @InjectMocks
    private CoinGeckoApiService coinGeckoApiService;

    private CryptoCurrency currency;
    private List<String> filters;

    @BeforeEach
    void setUp() {
        currency = new CryptoCurrency("BTC", "bitcoin");
        filters = List.of("eth", "xrp");
    }

    @Test
    void fetchRates() {
        Map<String, Object> apiResponse = Map.of("bitcoin", Map.of("eth", 30, "xrp", 100));
        when(apiClient.fetchRates(currency, filters)).thenReturn(apiResponse);
        Map<String, BigDecimal> processedRates = Map.of("eth", BigDecimal.valueOf(30), "xrp", BigDecimal.valueOf(100));
        when(rateProcessor.processRates(anyMap())).thenReturn(processedRates);

        CurrencyRateResponse response = coinGeckoApiService.fetchRates(currency, filters);

        assertNotNull(response);
        assertEquals("BTC", response.source());
        assertTrue(response.rates().containsKey("eth"));
        assertTrue(response.rates().containsKey("xrp"));
        assertEquals(BigDecimal.valueOf(30), response.rates().get("eth"));
        assertEquals(BigDecimal.valueOf(100), response.rates().get("xrp"));
    }

}