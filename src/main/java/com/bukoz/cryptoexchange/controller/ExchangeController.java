package com.bukoz.cryptoexchange.controller;

import com.bukoz.cryptoexchange.model.ExchangeRequest;
import com.bukoz.cryptoexchange.model.ExchangeResponse;
import com.bukoz.cryptoexchange.service.ExchangeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/currencies")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping("/exchange")
    public ResponseEntity<ExchangeResponse> getExchange(@RequestBody @Valid ExchangeRequest request) throws IOException, URISyntaxException {
        log.info("Currency exchange POST call");
        ExchangeResponse response = exchangeService.getExchange(request);
        return ResponseEntity.ok(response);
    }
}
