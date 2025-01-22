package com.bukoz.cryptoexchange.externalapi;

import com.bukoz.cryptoexchange.model.CryptoCurrency;

import java.util.List;
import java.util.Map;

public interface CurrencyRateApiClient {

    Map<String, Object> fetchRates(CryptoCurrency currency, List<String> filters);
}
