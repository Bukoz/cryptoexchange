package com.bukoz.cryptoexchange.model;

import java.util.Map;

public record ValidationErrorResponse(
        String message,
        Map<String, String> details) {
}
