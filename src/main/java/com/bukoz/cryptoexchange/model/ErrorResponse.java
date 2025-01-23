package com.bukoz.cryptoexchange.model;

import java.util.Map;

public record ErrorResponse(
        String message,
        Map<String, String> details) {
}
