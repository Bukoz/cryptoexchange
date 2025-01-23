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

    private static final String XRP = "xrp";
    private static final String BITCOIN = "bitcoin";
    private static final String ETH = "eth";
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
        currency = new CryptoCurrency("BTC", BITCOIN);
        filters = List.of(ETH, XRP);
    }

    @Test
    void fetchRates() {
        Map<String, Object> apiResponse = Map.of(BITCOIN, Map.of(ETH, 30, XRP, 100));
        when(apiClient.fetchRates(currency, filters)).thenReturn(apiResponse);
        Map<String, BigDecimal> processedRates = Map.of(ETH, BigDecimal.valueOf(30), XRP, BigDecimal.valueOf(100));
        when(rateProcessor.processRates(anyMap())).thenReturn(processedRates);

        CurrencyRateResponse response = coinGeckoApiService.fetchRates(currency, filters);

        assertNotNull(response);
        assertEquals("BTC", response.source());
        assertTrue(response.rates().containsKey(ETH));
        assertTrue(response.rates().containsKey(XRP));
        assertEquals(BigDecimal.valueOf(30), response.rates().get(ETH));
        assertEquals(BigDecimal.valueOf(100), response.rates().get(XRP));
    }

}