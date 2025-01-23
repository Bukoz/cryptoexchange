package com.bukoz.cryptoexchange.model;

public record CurrencyErrorResponse(
        String message,
        String details
) {
}
