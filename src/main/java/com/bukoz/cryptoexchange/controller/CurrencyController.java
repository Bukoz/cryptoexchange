package com.bukoz.cryptoexchange.controller;

import com.bukoz.cryptoexchange.model.CurrencyRate;
import com.bukoz.cryptoexchange.model.ExchangeRequest;
import com.bukoz.cryptoexchange.model.ExchangeResponse;
import com.bukoz.cryptoexchange.service.CurrencyService;
import com.bukoz.cryptoexchange.service.ExchangeService;
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
    private final ExchangeService exchangeService;

    public CurrencyController(CurrencyService currencyService, ExchangeService exchangeService) {
        this.currencyService = currencyService;
        this.exchangeService = exchangeService;
    }

    @GetMapping("/{currency}")
    public ResponseEntity<CurrencyRate> getRates(
            @PathVariable String currency,
            @RequestParam(value = "filter[]", required = false) List<String> filters) throws IOException, URISyntaxException {
        log.info("Currency rate GET call for {} with filter {}", currency, filters);
        CurrencyRate response = currencyService.getCurrencyRates(currency, filters);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/exchange")
    public ResponseEntity<ExchangeResponse> getExchange(@RequestBody ExchangeRequest request) throws IOException, URISyntaxException {
        log.info("Currency exchange POST call");
        ExchangeResponse response = exchangeService.getExchange(request);
        return ResponseEntity.ok(response);
    }

}
