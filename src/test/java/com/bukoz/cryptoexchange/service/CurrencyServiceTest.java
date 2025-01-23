package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.exception.internal.UnsupportedCurrencyException;
import com.bukoz.cryptoexchange.externalapi.common.CurrencyHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private CurrencyHandler currencyHandler;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void getCurrencyByShortNameSuccess() {
        CryptoCurrency currency = new CryptoCurrency("BTC", "bitcoin");
        when(currencyHandler.getByShortName(anyString())).thenReturn(currency);

        var result = currencyService.getCurrency("btc");

        assertNotNull(result);
        assertEquals("BTC", result.shortName());
    }

    @Test
    void getCurrencyByLongNameSuccess() {
        CryptoCurrency currency = new CryptoCurrency("BTC", "bitcoin");
        when(currencyHandler.getByShortName(anyString())).thenReturn(null);
        when(currencyHandler.getByLongName(anyString())).thenReturn(currency);

        var result = currencyService.getCurrency("bitcoin");

        assertNotNull(result);
        assertEquals("bitcoin", result.longName());
    }

    @Test
    void getCurrencyThrowsUnsupportedCurrencyException() {
        when(currencyHandler.getByShortName(anyString())).thenReturn(null);
        when(currencyHandler.getByLongName(anyString())).thenReturn(null);

        UnsupportedCurrencyException ex = assertThrows(UnsupportedCurrencyException.class, () -> currencyService.getCurrency("test"));

        assertEquals("TEST currency not supported", ex.getMessage());
    }

    @Test
    void getAllCurrencies() {
        when(currencyHandler.getAllCurrencies()).thenReturn(List.of(new CryptoCurrency("BTC", "bitcoin")));

        var result = currencyService.getAllCurrencies();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}