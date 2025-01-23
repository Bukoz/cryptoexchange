package com.bukoz.cryptoexchange.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record ExchangeRequest(
        @NotEmpty(message = "'from' field is required") String from,
        @NotEmpty(message = "'to' must contain at least one target currency") List<String> to,
        @NotNull(message = "'amount' field is required") @Positive(message = "'amount' should be greater than 0") BigDecimal amount) {
}
