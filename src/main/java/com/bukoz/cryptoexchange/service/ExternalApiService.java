package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.model.CurrencyRate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface ExternalApiService {

    CurrencyRate fetchRates(String currency, List<String> filters) throws IOException, URISyntaxException;
}
