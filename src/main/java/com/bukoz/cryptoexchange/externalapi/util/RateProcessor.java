package com.bukoz.cryptoexchange.externalapi.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class RateProcessor {

    public Map<String, BigDecimal> processRates(Map<String, Object> rawRates) {
        Map<String, BigDecimal> rates = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : rawRates.entrySet()) {
            Object rate = entry.getValue();
            BigDecimal value = toBigDecimal(rate);
            rates.put(entry.getKey(), value);
        }
        return rates;
    }

    private BigDecimal toBigDecimal(Object rate) {
        if (rate instanceof String value) {
            return new BigDecimal(value);
        } else if (rate instanceof Number value) {
            return BigDecimal.valueOf(value.doubleValue());
        } else {
            throw new IllegalArgumentException(String.format(
                    "Unsupported rate type: %s", rate.getClass().getName()));
        }
    }
}
