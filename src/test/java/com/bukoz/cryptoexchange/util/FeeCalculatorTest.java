package com.bukoz.cryptoexchange.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FeeCalculatorTest {

    @Test
    void calculateFee() {
        BigDecimal fee = BigDecimal.valueOf(0.02);
        FeeCalculator feeCalculator = new FeeCalculator(fee);

        var result = feeCalculator.calculateFee(BigDecimal.valueOf(10));

        assertEquals(BigDecimal.valueOf(0.2), result.stripTrailingZeros());
    }
}