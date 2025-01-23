package com.bukoz.cryptoexchange.controller;

import com.bukoz.cryptoexchange.model.CryptoCurrency;
import com.bukoz.cryptoexchange.model.CurrencyRateResponse;
import com.bukoz.cryptoexchange.service.CurrencyRateService;
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

    private final CurrencyRateService currencyRateService;
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyRateService currencyService, CurrencyService currencyHandlerService) {
        this.currencyRateService = currencyService;
        this.currencyService = currencyHandlerService;
    }

    @GetMapping("/{currency}")
    public ResponseEntity<CurrencyRateResponse> getRates(
            @PathVariable String currency,
            @RequestParam(value = "filter[]", required = false) List<String> filters) throws IOException, URISyntaxException {
        log.info("Currency rate GET call for {} with filter {}", currency, filters);

        CryptoCurrency baseCurrency = currencyService.getCurrency(currency);
        List<CryptoCurrency> filterCurrencies = filters == null
                ? currencyService.getAllCurrencies()
                : filters.stream()
                .map(currencyService::getCurrency)
                .toList();

        CurrencyRateResponse response = currencyRateService.getCurrencyRates(baseCurrency, filterCurrencies);
        return ResponseEntity.ok(response);
    }
}

