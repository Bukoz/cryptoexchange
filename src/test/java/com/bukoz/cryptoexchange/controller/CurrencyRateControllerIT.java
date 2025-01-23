package com.bukoz.cryptoexchange.controller;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.exception.internal.UnsupportedCurrencyException;
import com.bukoz.cryptoexchange.model.CurrencyRateResponse;
import com.bukoz.cryptoexchange.service.CurrencyRateService;
import com.bukoz.cryptoexchange.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyRateController.class)
class CurrencyRateControllerIT {

    private static final String BITCOIN = "bitcoin";
    private static final String BTC = "BTC";
    private static final String ETH = "ETH";
    private static final String ETHEREUM = "ethereum";
    private static final String CURRENCIES_URL = "/currencies/{currency}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CurrencyRateService currencyRateService;

    @MockBean
    private CurrencyService currencyService;

    @Test
    void getRatesWhenSquareBracketsInParamSuccess() throws Exception {
        mockBehaviorForSuccessTest();

        mockMvc.perform(get(CURRENCIES_URL, BITCOIN)
                        .param("filter[]", ETH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.source").exists())
                .andExpect(jsonPath("$.source").value(BITCOIN))
                .andExpect(jsonPath("$.rates").exists())
                .andExpect(jsonPath("$.rates.ETH").value(BigDecimal.valueOf(30)));
    }


    @Test
    void getRatesWhenNoSquareBracketsInParamSuccess() throws Exception {
        mockBehaviorForSuccessTest();

        mockMvc.perform(get(CURRENCIES_URL, BITCOIN)
                        .param("filter", ETH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.source").exists())
                .andExpect(jsonPath("$.source").value(BITCOIN))
                .andExpect(jsonPath("$.rates").exists())
                .andExpect(jsonPath("$.rates.ETH").value(BigDecimal.valueOf(30)));
    }

    private void mockBehaviorForSuccessTest() throws IOException, URISyntaxException {
        CryptoCurrency baseCurrency = new CryptoCurrency(BTC, BITCOIN);
        List<CryptoCurrency> filterCurrencies = List.of(new CryptoCurrency(ETH, ETHEREUM));
        CurrencyRateResponse response = new CurrencyRateResponse(BITCOIN, Map.of(ETH, BigDecimal.valueOf(30)));
        when(currencyService.getCurrency(BITCOIN)).thenReturn(baseCurrency);
        when(currencyService.getCurrency(ETH)).thenReturn(new CryptoCurrency(ETH, ETHEREUM));
        when(currencyRateService.getCurrencyRates(baseCurrency, filterCurrencies)).thenReturn(response);
    }

    @Test
    void getRatesCurrencyNotFound() throws Exception {
        String currency = "testCurrency";
        when(currencyService.getCurrency(currency)).thenThrow(new UnsupportedCurrencyException(currency + " not supported"));

        mockMvc.perform(get(CURRENCIES_URL, currency)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Currency not found"))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.detail").value("testCurrency not supported"));
    }

}