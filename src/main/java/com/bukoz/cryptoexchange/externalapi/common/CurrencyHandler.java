package com.bukoz.cryptoexchange.externalapi.common;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;

import java.util.List;

public interface CurrencyHandler {

    CryptoCurrency getByShortName(String shortName);

    CryptoCurrency getByLongName(String longName);

    List<CryptoCurrency> getAllCurrencies();
}

