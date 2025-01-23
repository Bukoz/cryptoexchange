package com.bukoz.cryptoexchange.externalapi.common;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;

import java.util.List;
import java.util.Map;

public interface CurrencyRateApiClient {

    Map<String, Object> fetchRates(CryptoCurrency currency, List<String> filters);
}
