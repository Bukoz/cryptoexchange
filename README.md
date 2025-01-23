# Crypto Exchange

## Task for Gamedia interview process

# Table of Contents

- [Requirements](#requirements)
- [Notes](#notes)
- [Build](#build)
- [Verify](#verify)
- [Spring Boot Run](#spring-boot-run)
- [API Usage](#api-usage)
    - [API Endpoints](#api-endpoints)
        - [GET Request](#get)
        - [POST Request](#post)
    - [Supported Crypto](#supported-crypto-names-due-to-limitations-of-coingecko-api)
- [File structure](#file-structure)
- [Dependencies](#dependencies)

## Requirements

**Java 17**

## Notes

**External crypto API used in application** - [CoinGecko](https://www.coingecko.com/en/api)

## Build

Build the application to download required dependencies:

```
mvn clean package
```

## Verify

To verify Unit Tests and Integration Tests run:

```
mvn verify
```

![](img/verify.gif)

## Spring Boot Run

To run the application

- start it from your IDE
- use Maven or Maven Wrapper

```
mvn spring-boot:run
```

![](img/run.gif)

## API Usage

API is available through [Swagger](http://localhost:8080/swagger-ui/index.html)

```
http://localhost:8080/swagger-ui/index.html
```

### API Endpoints:

#### GET

/currencies/{currency}?filter= (filter is not mandatory)

**Example usage with cURL**

```
curl -X 'GET' \
  'http://localhost:8080/currencies/btc?filter=xrp&filter=eth' \
  -H 'accept: application/json'
```

**Example output:**

```
{
  "source": "BTC",
  "rates": {
    "XRP": 33750,
    "ETH": 32.432179
  }
}
```

#### POST

/exchange

**Request body**

```
{
  "from": "currencyA",
  "to": [
    "currencyB",
    "currencyC",
    ...
  ],
  "amount": 123
}
```

**Example usage with cURL**

```
curl -X 'POST' \
  'http://localhost:8080/currencies/exchange' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "from": "btc",
  "to": [
    "eth",
    "xrp"
  ],
  "amount": 1
}'

```

**Example output:**

```
{
  "from": "btc",
  "forecasts": {
    "eth": {
      "rate": 32.435697,
      "amount": 1,
      "result": 32.11134003,
      "fee": 0.01
    },
    "xrp": {
      "rate": 33764,
      "amount": 1,
      "result": 33426.36,
      "fee": 0.01
    }
  },
  "note": "Each forecast is calculated after subtracting fee from original currency (btc)"
}
```

### Supported crypto names due to limitations of CoinGecko API

Both names of specific currency can be used in API calls

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

## Dependencies:

- Spring Boot (3.3.7)
- Lombok (1.8.36)
- SpringDoc OpenAPI Starter WebMVC UI (2.5.0)
- Maven Failsafe Plugin (3.5.2)
