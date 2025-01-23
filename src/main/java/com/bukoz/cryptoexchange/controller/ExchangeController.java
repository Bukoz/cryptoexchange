package com.bukoz.cryptoexchange.controller;

import com.bukoz.cryptoexchange.model.*;
import com.bukoz.cryptoexchange.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Perform currency exchange",
            description = "This endpoint processes a currency exchange based on the provided request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful currency exchange",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExchangeResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request due to validation errors",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "Currency not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CurrencyErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InternalErrorResponse.class)))
    })
    @PostMapping("/exchange")
    public ResponseEntity<ExchangeResponse> getExchange(@RequestBody @Valid ExchangeRequest request) throws IOException, URISyntaxException {
        log.debug("Currency exchange POST call");
        ExchangeResponse response = exchangeService.getExchange(request);
        return ResponseEntity.ok(response);
    }
}
