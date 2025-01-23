package com.bukoz.cryptoexchange.model;

public record InternalErrorResponse(
        String message,
        String details
) {
}
