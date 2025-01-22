package com.bukoz.cryptoexchange.externalapi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface SupportedCurrenciesApiClient {
    
    List<String> fetchSupportedCurrencies() throws IOException, URISyntaxException;
}
