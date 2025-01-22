package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.model.CryptoCurrency;
import com.bukoz.cryptoexchange.model.CurrencyRateResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface ExternalApiService {

    CurrencyRateResponse fetchRates(CryptoCurrency currency, List<String> filters) throws IOException, URISyntaxException;
}
