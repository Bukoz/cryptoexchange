package com.bukoz.cryptoexchange.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@Getter
public class ExchangeResponse {
    private String from;
    private Map<String, CurrencyExchangeForecast> forecasts;
    private String note;

    @Builder
    @Getter
    public static class CurrencyExchangeForecast {
        private BigDecimal rate;
        private BigDecimal amount;
        private BigDecimal result;
        private BigDecimal fee;
    }
}
