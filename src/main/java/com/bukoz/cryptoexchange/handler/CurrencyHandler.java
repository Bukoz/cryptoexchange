package com.bukoz.cryptoexchange.handler;

import com.bukoz.cryptoexchange.model.CryptoCurrency;

import java.util.List;

public interface CurrencyHandler {

    CryptoCurrency getByShortName(String shortName);

    CryptoCurrency getByLongName(String longName);

    List<CryptoCurrency> getAllCurrencies();
}

