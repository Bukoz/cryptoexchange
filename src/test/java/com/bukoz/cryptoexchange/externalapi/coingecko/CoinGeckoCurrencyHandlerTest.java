package com.bukoz.cryptoexchange.externalapi.coingecko;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoinGeckoCurrencyHandlerTest {

    private static final String BTC = "BTC";
    private static final String ETH = "ETH";
    private CoinGeckoCurrencyHandler currencyHandler;

    @BeforeEach
    void setUp() {
        currencyHandler = new CoinGeckoCurrencyHandler();
    }

    @Test
    void getByShortName() {
        CryptoCurrency result = currencyHandler.getByShortName(BTC);

        assertNotNull(result);
        assertEquals(BTC, result.shortName());
        assertEquals("bitcoin", result.longName());
    }

    @Test
    void getByShortNameNotFound() {
        String shortName = "TEST";

        CryptoCurrency result = currencyHandler.getByShortName(shortName);

        assertNull(result);
    }

    @Test
    void getByLongName() {
        String longName = "ethereum";

        CryptoCurrency result = currencyHandler.getByLongName(longName);

        assertNotNull(result);
        assertEquals(ETH, result.shortName());
        assertEquals("ethereum", result.longName());
    }

    @Test
    void getByLongNameNotFound() {
        String longName = "testCoin";

        CryptoCurrency result = currencyHandler.getByLongName(longName);

        assertNull(result);
    }

    @Test
    void getAllCurrencies() {
        List<CryptoCurrency> result = currencyHandler.getAllCurrencies();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(c -> BTC.equals(c.shortName())));
        assertTrue(result.stream().anyMatch(c -> ETH.equals(c.shortName())));
    }

}