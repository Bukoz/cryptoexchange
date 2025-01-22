package com.bukoz.cryptoexchange.model;

import java.math.BigDecimal;
import java.util.Map;

public record CurrencyRateResponse(
        String source,
        Map<String, BigDecimal> rates
) {
}
