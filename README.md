# Crypto Exchange

## Task for Gamedia interview process

---

## Build

Build the application to download required dependencies:
``
mvn clean package
``

---

## Verify

To verify Unit Tests and Integration Tests run:
``mvn verify``

![](img/verify.gif)

---

## Spring Boot Run

To run the application

- start it from your IDE
- use Maven or Maven Wrapper
  ``
  mvn spring-boot:run
  ``

![](img/run.gif)

---

## API Usage

API is available through [Swagger](http://localhost:8080/swagger-ui/index.html)

``
http://localhost:8080/swagger-ui/index.html
``

API Endpoints:

- GET /currencies/{currency}?filter=
- POST /exchange

External crypto API - [CoinGecko](https://www.coingecko.com/en/api)

Supported crypto names - either name can be used in API calls.
(due to limitations of CoinGecko API this list had to be reduced)

```
BTC, bitcoin
ETH, ethereum
LTC, litecoin
BCH, bitcoin-cash
BNB, binancecoin
EOS, eos
XRP, ripple
XLM, stellar
LINK, chainlink
DOT, polkadot
YFI, yearn-finance
```

---

## File structure

```
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── bukoz
    │   │           └── cryptoexchange
    │   │               ├── CryptoExchangeApplication.java
    │   │               ├── config
    │   │               │   └── RestTemplateConfig.java
    │   │               ├── controller
    │   │               │   ├── CurrencyRateController.java
    │   │               │   └── ExchangeController.java
    │   │               ├── domain
    │   │               │   └── CryptoCurrency.java
    │   │               ├── exception
    │   │               │   ├── GlobalExceptionHandler.java
    │   │               │   ├── external
    │   │               │   │   └── ExternalApiException.java
    │   │               │   └── internal
    │   │               │       └── UnsupportedCurrencyException.java
    │   │               ├── externalapi
    │   │               │   ├── coingecko
    │   │               │   │   ├── CoinGeckoCurrencyHandler.java
    │   │               │   │   ├── CoinGeckoCurrencyRateApiClient.java
    │   │               │   │   └── CoinGeckoPriceApiHelper.java
    │   │               │   └── common
    │   │               │       ├── CurrencyHandler.java
    │   │               │       └── CurrencyRateApiClient.java
    │   │               ├── model
    │   │               │   ├── CurrencyErrorResponse.java
    │   │               │   ├── CurrencyRateResponse.java
    │   │               │   ├── ExchangeRequest.java
    │   │               │   ├── ExchangeResponse.java
    │   │               │   ├── InternalErrorResponse.java
    │   │               │   └── ValidationErrorResponse.java
    │   │               ├── service
    │   │               │   ├── CoinGeckoApiService.java
    │   │               │   ├── CurrencyRateService.java
    │   │               │   ├── CurrencyService.java
    │   │               │   ├── ExchangeForecastService.java
    │   │               │   ├── ExchangeService.java
    │   │               │   └── ExternalApiService.java
    │   │               └── util
    │   │                   ├── FeeCalculator.java
    │   │                   └── RateProcessor.java
    │   └── resources
    │       ├── application.yml
    │       └── banner.txt
    └── test
        └── java
            └── com
                └── bukoz
                    └── cryptoexchange
                        ├── CryptoExchangeApplicationTests.java
                        ├── controller
                        │   ├── CurrencyRateControllerIT.java
                        │   └── ExchangeControllerIT.java
                        ├── exception
                        │   ├── GlobalExceptionHandlerIT.java
                        │   └── TestController.java
                        ├── externalapi
                        │   └── coingecko
                        │       ├── CoinGeckoCurrencyHandlerTest.java
                        │       ├── CoinGeckoCurrencyRateApiClientTest.java
                        │       └── CoinGeckoPriceApiHelperTest.java
                        ├── service
                        │   ├── CoinGeckoApiServiceTest.java
                        │   ├── CurrencyRateServiceTest.java
                        │   ├── CurrencyServiceTest.java
                        │   ├── ExchangeForecastServiceTest.java
                        │   └── ExchangeServiceTest.java
                        └── util
                            └── FeeCalculatorTest.java
```

---

## Dependencies:

- Java 17
- Spring Boot (3.3.7)
- Lombok (1.8.36)
- SpringDoc OpenAPI Starter WebMVC UI (2.5.0)
- Maven Failsafe Plugin (3.5.2)
