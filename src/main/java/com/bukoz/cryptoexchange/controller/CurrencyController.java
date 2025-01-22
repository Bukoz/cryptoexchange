package com.bukoz.cryptoexchange.controller;

import com.bukoz.cryptoexchange.model.CurrencyRate;
import com.bukoz.cryptoexchange.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/{currency}")
    public ResponseEntity<CurrencyRate> getRates(
            @PathVariable String currency,
            @RequestParam(value = "filter[]", required = false) List<String> filters) throws IOException, URISyntaxException {
        log.info("Currency rate GET call for {} with filter {}", currency, filters);
        CurrencyRate response = currencyService.getCurrencyRates(currency, filters);
        return ResponseEntity.ok(response);
    }

}
