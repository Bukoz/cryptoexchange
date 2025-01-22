package com.bukoz.cryptoexchange.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FeeCalculator {

    private final BigDecimal feePercentage;

    public FeeCalculator(@Value("${fee.percentage}") BigDecimal feePercentage) {
        this.feePercentage = feePercentage;
    }

    public BigDecimal calculateFee(BigDecimal amount) {
        return feePercentage.multiply(amount);
    }
}
