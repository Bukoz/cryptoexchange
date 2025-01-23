package com.bukoz.cryptoexchange.controller;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.exception.internal.UnsupportedCurrencyException;
import com.bukoz.cryptoexchange.model.CurrencyRateResponse;
import com.bukoz.cryptoexchange.service.CurrencyRateService;
import com.bukoz.cryptoexchange.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @MockBean
    private CurrencyRateService currencyRateService;

    @MockBean
    private CurrencyService currencyService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getRatesSuccess() throws Exception {
        CryptoCurrency baseCurrency = new CryptoCurrency(BTC, BITCOIN);
        List<CryptoCurrency> filterCurrencies = List.of(new CryptoCurrency(ETH, ETHEREUM));
        CurrencyRateResponse response = new CurrencyRateResponse(BITCOIN, Map.of(ETH, BigDecimal.valueOf(30)));
        when(currencyService.getCurrency(BITCOIN)).thenReturn(baseCurrency);
        when(currencyService.getCurrency(ETH)).thenReturn(new CryptoCurrency(ETH, ETHEREUM));
        when(currencyRateService.getCurrencyRates(baseCurrency, filterCurrencies)).thenReturn(response);

        var result = mockMvc.perform(get(CURRENCIES_URL, BITCOIN)
                        .param("filter[]", ETH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.source").exists())
                .andExpect(jsonPath("$.rates").exists())
                .andReturn();

        String source = JsonPath.read(result.getResponse().getContentAsString(), "$.source");

        assertEquals(BITCOIN, source);
    }

    @Test
    void getRatesCurrencyNotFound() throws Exception {
        String currency = "testCurrency";
        when(currencyService.getCurrency(currency)).thenThrow(new UnsupportedCurrencyException(currency + " not supported"));

        var result = mockMvc.perform(get(CURRENCIES_URL, currency)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.detail").exists())
                .andReturn();

        String message = JsonPath.read(result.getResponse().getContentAsString(), "$.message");
        String detail = JsonPath.read(result.getResponse().getContentAsString(), "$.detail");

        assertEquals("Currency not found", message);
        assertEquals("testCurrency not supported", detail);
    }

}