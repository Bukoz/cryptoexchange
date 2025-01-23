package com.bukoz.cryptoexchange;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "CryptoAPI",
                version = "1.0",
                description = "API documentation for CryptoExchange APP",
                contact = @Contact(name = "Jakub Z. (bukoz)")
        )
)
@SpringBootApplication
public class CryptoExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoExchangeApplication.class, args);
    }

}
