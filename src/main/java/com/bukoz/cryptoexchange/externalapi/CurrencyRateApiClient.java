package com.bukoz.cryptoexchange.externalapi;

import java.util.List;
import java.util.Map;

public interface CurrencyRateApiClient {

    Map<String, Object> fetchRates(String currency, List<String> filters);
}
