package com.bukoz.cryptoexchange.controller;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.model.CurrencyErrorResponse;
import com.bukoz.cryptoexchange.model.CurrencyRateResponse;
import com.bukoz.cryptoexchange.service.CurrencyRateService;
import com.bukoz.cryptoexchange.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/currencies")
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;
    private final CurrencyService currencyService;

    public CurrencyRateController(CurrencyRateService currencyService, CurrencyService currencyHandlerService) {
        this.currencyRateService = currencyService;
        this.currencyService = currencyHandlerService;
    }

    @Operation(summary = "Get currency rates", description = "Fetches currency rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CurrencyRateResponse.class))),
            @ApiResponse(responseCode = "404", description = "Currency not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CurrencyErrorResponse.class)))
    })
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

