package com.bukoz.cryptoexchange.controller;

import com.bukoz.cryptoexchange.exception.internal.UnsupportedCurrencyException;
import com.bukoz.cryptoexchange.model.ExchangeRequest;
import com.bukoz.cryptoexchange.model.ExchangeResponse;
import com.bukoz.cryptoexchange.service.ExchangeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExchangeController.class)
class ExchangeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeService exchangeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getExchangeSuccessfulResponse() throws Exception {
        Map<String, ExchangeResponse.CurrencyExchangeForecast> forecasts = Map.of(
                "ETH", ExchangeResponse.CurrencyExchangeForecast.builder()
                        .rate(BigDecimal.valueOf(0.9))
                        .amount(BigDecimal.valueOf(100))
                        .result(BigDecimal.valueOf(85.5))
                        .fee(BigDecimal.valueOf(5))
                        .build(),
                "XRP", ExchangeResponse.CurrencyExchangeForecast.builder()
                        .rate(BigDecimal.valueOf(110.0))
                        .amount(BigDecimal.valueOf(100))
                        .result(BigDecimal.valueOf(10450))
                        .fee(BigDecimal.valueOf(5))
                        .build()
        );
        ExchangeResponse mockResponse = new ExchangeResponse("BTC", forecasts, "Each forecast is calculated after subtracting fee from original currency (BTC)");
        when(exchangeService.getExchange(any(ExchangeRequest.class))).thenReturn(mockResponse);
        ExchangeRequest request = new ExchangeRequest("BTC", List.of("ETH", "XRP"), BigDecimal.valueOf(100));

        mockMvc.perform(post("/currencies/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("BTC"))
                .andExpect(jsonPath("$.forecasts.ETH.rate").value(0.9))
                .andExpect(jsonPath("$.forecasts.ETH.result").value(85.5))
                .andExpect(jsonPath("$.forecasts.XRP.rate").value(110.0))
                .andExpect(jsonPath("$.forecasts.XRP.result").value(10450))
                .andExpect(jsonPath("$.note").value("Each forecast is calculated after subtracting fee from original currency (BTC)"));
    }

    @Test
    void getExchangeBadRequest() throws Exception {
        ExchangeRequest invalidRequest = new ExchangeRequest("BTC", null, BigDecimal.valueOf(100));

        mockMvc.perform(post("/currencies/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getExchangeCurrencyNotFound() throws Exception {
        when(exchangeService.getExchange(any(ExchangeRequest.class)))
                .thenThrow(new UnsupportedCurrencyException("testCurrency not found"));
        ExchangeRequest request = new ExchangeRequest("BTC", List.of("testCurrency"), BigDecimal.valueOf(100));

        mockMvc.perform(post("/currencies/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Currency not found"))
                .andExpect(jsonPath("$.detail").value("testCurrency not found"));
    }

    @Test
    void getExchangeInternalServerError() throws Exception {
        when(exchangeService.getExchange(any(ExchangeRequest.class)))
                .thenThrow(new RuntimeException("Unexpected error"));
        ExchangeRequest request = new ExchangeRequest("BTC", List.of("ETH"), BigDecimal.valueOf(100));

        mockMvc.perform(post("/currencies/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal server error"))
                .andExpect(jsonPath("$.detail").value("Unexpected error"));
    }
}
