package com.bukoz.cryptoexchange.externalapi.coingecko;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoinGeckoPriceApiHelperTest {

    private static final String API_URL = "fake-api/price";

    @Test
    void buildPriceApiUrlWithFilters() {
        String currency = "bitcoin";
        List<String> filters = List.of("eth", "xrp");
        String expectedUrl = "fake-api/price?ids=bitcoin&vs_currencies=eth,xrp";

        String resultUrl = CoinGeckoPriceApiHelper.buildPriceApiUrl(currency, filters, API_URL);

        assertEquals(expectedUrl, resultUrl);
    }

    @Test
    void buildPriceApiUrlWithNoFilters() {
        String currency = "bitcoin";
        List<String> filters = new ArrayList<>();
        String expectedUrl = "fake-api/price?ids=bitcoin&vs_currencies=";

        String resultUrl = CoinGeckoPriceApiHelper.buildPriceApiUrl(currency, filters, API_URL);

        assertEquals(expectedUrl, resultUrl);
    }

    @Test
    void buildPriceApiUrlWithSingleFilter() {
        String currency = "bitcoin";
        List<String> filters = List.of("eth");
        String expectedUrl = "fake-api/price?ids=bitcoin&vs_currencies=eth";

        String resultUrl = CoinGeckoPriceApiHelper.buildPriceApiUrl(currency, filters, API_URL);

        assertEquals(expectedUrl, resultUrl);
    }

    @Test
    void buildPriceApiUrlWithEmptyCurrency() {
        String currency = "";
        List<String> filters = List.of("usd", "eur");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> CoinGeckoPriceApiHelper.buildPriceApiUrl(currency, filters, API_URL));

        assertEquals("Currency value should not be empty in external API URL", ex.getMessage());
    }


    @Test
    void buildPriceApiUrlWithSingleFilterUppercase() {
        String currency = "bitcoin";
        List<String> filters = List.of("XRP");
        String expectedUrl = "fake-api/price?ids=bitcoin&vs_currencies=xrp";

        String resultUrl = CoinGeckoPriceApiHelper.buildPriceApiUrl(currency, filters, API_URL);

        assertEquals(expectedUrl, resultUrl);
    }

}