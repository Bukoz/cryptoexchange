package com.bukoz.cryptoexchange.externalapi.coingecko;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.exception.external.ExternalApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class CoinGeckoCurrencyRateApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CoinGeckoCurrencyRateApiClient coinGeckoCurrencyRateApiClient;

    @Test
    void fetchRatesWithValidResponse() {
        CryptoCurrency cryptoCurrency = new CryptoCurrency("BTC", "bitcoin");
        List<String> filters = List.of("eth");
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("bitcoin", Map.of("eth", 30));
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(mockResponse);

        Map<String, Object> result = coinGeckoCurrencyRateApiClient.fetchRates(cryptoCurrency, filters);

        assertNotNull(result);
        assertTrue(result.containsKey("bitcoin"));
        assertEquals(30, ((Map<?, ?>) result.get("bitcoin")).get("eth"));
    }

    @Test
    void fetchRatesWithNoFilters() {
        CryptoCurrency cryptoCurrency = new CryptoCurrency("BTC", "bitcoin");
        List<String> filters = new ArrayList<>();
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("bitcoin", Map.of("eth", 30));
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(mockResponse);

        Map<String, Object> result = coinGeckoCurrencyRateApiClient.fetchRates(cryptoCurrency, filters);

        assertNotNull(result);
        assertTrue(result.containsKey("bitcoin"));
        assertEquals(30, ((Map<?, ?>) result.get("bitcoin")).get("eth"));
    }

    @Test
    void fetchRatesWithEmptyResponse() {
        CryptoCurrency cryptoCurrency = new CryptoCurrency("BTC", "bitcoin");
        List<String> filters = List.of("eth");
        Map<String, Object> mockResponse = new HashMap<>(); // Empty response
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(mockResponse);

        ExternalApiException exception = assertThrows(ExternalApiException.class, () -> {
            coinGeckoCurrencyRateApiClient.fetchRates(cryptoCurrency, filters);
        });
        assertEquals("Received empty response from external API", exception.getMessage());
    }


}
