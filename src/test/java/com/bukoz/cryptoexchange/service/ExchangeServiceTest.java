package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.model.CurrencyRateResponse;
import com.bukoz.cryptoexchange.model.ExchangeRequest;
import com.bukoz.cryptoexchange.model.ExchangeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceTest {

    @Mock
    private ExternalApiService externalApiService;
    @Mock
    private CurrencyService currencyService;
    @Mock
    private ExchangeForecastService exchangeForecastService;
    @InjectMocks
    private ExchangeService exchangeService;


    @Test
    void getExchange() throws IOException, URISyntaxException {
        CryptoCurrency currency = new CryptoCurrency("BTC", "bitcoin");
        when(currencyService.getCurrency("BTC")).thenReturn(currency);
        List<String> targetCurrencies = List.of("ETH", "XRP");
        when(currencyService.getCurrency("ETH")).thenReturn(new CryptoCurrency("ETH", "ethereum"));
        when(currencyService.getCurrency("XRP")).thenReturn(new CryptoCurrency("xrp", "ripple"));
        Map<String, BigDecimal> rates = Map.of(
                "ETH", BigDecimal.valueOf(30.0),
                "XRP", BigDecimal.valueOf(100.0)
        );
        when(externalApiService.fetchRates(eq(currency), any()))
                .thenReturn(new CurrencyRateResponse(currency.shortName(), rates));
        ExchangeRequest request = new ExchangeRequest("BTC", targetCurrencies, BigDecimal.valueOf(100));
        Map<String, ExchangeResponse.CurrencyExchangeForecast> forecasts = Map.of(
                "ETH", ExchangeResponse.CurrencyExchangeForecast.builder()
                        .rate(BigDecimal.valueOf(0.9))
                        .amount(BigDecimal.valueOf(100))
                        .result(BigDecimal.valueOf(85.5)) // Mocked result
                        .fee(BigDecimal.valueOf(5))
                        .build(),
                "XRP", ExchangeResponse.CurrencyExchangeForecast.builder()
                        .rate(BigDecimal.valueOf(110.0))
                        .amount(BigDecimal.valueOf(100))
                        .result(BigDecimal.valueOf(10450)) // Mocked result
                        .fee(BigDecimal.valueOf(5))
                        .build()
        );
        when(exchangeForecastService.getCurrencyExchangeForecasts(request, rates)).thenReturn(forecasts);

        ExchangeResponse response = exchangeService.getExchange(request);

        assertEquals("BTC", response.getFrom());
        assertEquals(forecasts, response.getForecasts());
        assertEquals("Each forecast is calculated after subtracting fee from original currency (BTC)", response.getNote());
    }

    @Test
    void getExchangeThrowsIOException() throws IOException, URISyntaxException {
        ExchangeRequest request = mockExchangeRequest();
        when(externalApiService.fetchRates(any(), any())).thenThrow(new IOException("IOExc was thrown"));

        IOException ex = assertThrows(IOException.class, () -> exchangeService.getExchange(request));

        assertEquals("IOExc was thrown", ex.getMessage());
    }


    @Test
    void getExchangeThrowsURISyntaxException() throws IOException, URISyntaxException {
        ExchangeRequest request = mockExchangeRequest();
        when(externalApiService.fetchRates(any(), any())).thenThrow(new URISyntaxException("exc", "URIExc was thrown"));

        URISyntaxException ex = assertThrows(URISyntaxException.class, () -> exchangeService.getExchange(request));

        assertEquals("URIExc was thrown", ex.getReason());
    }

    private ExchangeRequest mockExchangeRequest() {
        CryptoCurrency currency = new CryptoCurrency("BTC", "bitcoin");
        when(currencyService.getCurrency("BTC")).thenReturn(currency);
        List<String> targetCurrencies = List.of("ETH", "XRP");
        when(currencyService.getCurrency("ETH")).thenReturn(new CryptoCurrency("ETH", "ethereum"));
        when(currencyService.getCurrency("XRP")).thenReturn(new CryptoCurrency("xrp", "ripple"));
        return new ExchangeRequest("BTC", targetCurrencies, BigDecimal.valueOf(100));
    }
}
