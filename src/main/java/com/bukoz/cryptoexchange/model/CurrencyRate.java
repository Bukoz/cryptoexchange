package com.bukoz.cryptoexchange.model;

import java.math.BigDecimal;
import java.util.Map;

public record CurrencyRate(
        String source,
        Map<String, BigDecimal> rates
) {
}
