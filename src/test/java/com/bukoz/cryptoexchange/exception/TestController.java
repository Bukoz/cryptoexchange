package com.bukoz.cryptoexchange.exception;

import com.bukoz.cryptoexchange.exception.external.ExternalApiException;
import com.bukoz.cryptoexchange.exception.internal.UnsupportedCurrencyException;
import com.bukoz.cryptoexchange.model.ExchangeRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping("/unsupported-currency")
    public void unsupportedCurrency() {
        throw new UnsupportedCurrencyException("Unsupported currency: XYZ");
    }

    @GetMapping("/external-api-error")
    public void externalApiError() {
        throw new ExternalApiException("External API is down");
    }

    @PostMapping("/validation-error")
    public void validationError(@RequestBody @Valid ExchangeRequest request) {
        // This method is intentionally empty. Validation errors will trigger automatically.
    }

    @GetMapping("/general-error")
    public void generalError() {
        throw new RuntimeException("An unexpected error occurred");
    }
}
