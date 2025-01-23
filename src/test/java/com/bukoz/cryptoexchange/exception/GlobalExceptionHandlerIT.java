package com.bukoz.cryptoexchange.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleUnsupportedCurrencyException() throws Exception {
        mockMvc.perform(get("/test/unsupported-currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Currency not found"))
                .andExpect(jsonPath("$.details").value("Unsupported currency: XYZ"));
    }

    @Test
    void handleExternalApiException() throws Exception {
        mockMvc.perform(get("/test/external-api-error")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("External API is down"));
    }

    @Test
    void handleValidationExceptions() throws Exception {
        String invalidRequestBody = """
                {
                    "from": "",
                    "to": []
                }
                """;

        mockMvc.perform(post("/test/validation-error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details.from").value("'from' field is required"))
                .andExpect(jsonPath("$.details.to").value("'to' must contain at least one target currency"))
                .andExpect(jsonPath("$.details.amount").value("'amount' field is required"));
    }

    @Test
    void handleGeneralException() throws Exception {
        mockMvc.perform(get("/test/general-error")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Internal server error"))
                .andExpect(jsonPath("$.details").value("An unexpected error occurred"));
    }
}