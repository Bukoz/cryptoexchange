package com.bukoz.cryptoexchange.externalapi.client;

import java.util.List;
import java.util.Map;

public interface ExternalApiClient {
    
    Map<String, Object> fetchRawRates(String currency, List<String> filters);
}
