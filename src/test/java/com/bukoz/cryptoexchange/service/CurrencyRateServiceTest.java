package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.model.CurrencyRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyRateServiceTest {

    private static final String BTC = "BTC";
    private static final String ETH = "ETH";
    private static final String LTC = "LTC";
    @Mock
    private ExternalApiService externalApiService;

    @InjectMocks
    private CurrencyRateService currencyRateService;

    private CryptoCurrency baseCurrency;
    private List<CryptoCurrency> filters;

    @BeforeEach
    void setUp() {
        baseCurrency = new CryptoCurrency(BTC, "bitcoin");
        filters = List.of(new CryptoCurrency(ETH, "ethereum"), new CryptoCurrency(LTC, "litecoin"));
    }

    @Test
    void getCurrencyRatesSuccess() throws IOException, URISyntaxException {
        CurrencyRateResponse expectedResponse = new CurrencyRateResponse(BTC, Map.of(ETH, BigDecimal.valueOf(30), LTC, BigDecimal.valueOf(150)));
        when(externalApiService.fetchRates(baseCurrency, List.of(ETH, LTC))).thenReturn(expectedResponse);

        CurrencyRateResponse response = currencyRateService.getCurrencyRates(baseCurrency, filters);

        assertNotNull(response);
        assertEquals(BTC, response.source());
        assertTrue(response.rates().containsKey(ETH));
        assertTrue(response.rates().containsKey(LTC));
        assertEquals(BigDecimal.valueOf(30), response.rates().get(ETH));
        assertEquals(BigDecimal.valueOf(150), response.rates().get(LTC));
    }

    @Test
    void getCurrencyRatesEmptyFiltersIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> currencyRateService.getCurrencyRates(baseCurrency, Collections.emptyList()));

        assertEquals("Filter list should not be empty", ex.getMessage());
    }

    @Test
    void getCurrencyRatesThrowsIOException() throws IOException, URISyntaxException {
        when(externalApiService.fetchRates(baseCurrency, List.of(ETH, LTC))).thenThrow(new IOException("API Error"));

        IOException exception = assertThrows(IOException.class, () -> {
            currencyRateService.getCurrencyRates(baseCurrency, filters);
        });

        assertEquals("API Error", exception.getMessage());
    }

    @Test
    void getCurrencyRatesThrowsURISyntaxException() throws IOException, URISyntaxException {
        when(externalApiService.fetchRates(baseCurrency, List.of(ETH, LTC))).thenThrow(new URISyntaxException("invalid", "Invalid URL"));

        URISyntaxException exception = assertThrows(URISyntaxException.class, () -> {
            currencyRateService.getCurrencyRates(baseCurrency, filters);
        });

        assertEquals("invalid", exception.getInput());
        assertEquals("Invalid URL", exception.getReason());
    }
}