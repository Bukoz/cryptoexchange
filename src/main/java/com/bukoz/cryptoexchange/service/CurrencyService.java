package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.exception.internal.UnsupportedCurrencyException;
import com.bukoz.cryptoexchange.handler.CurrencyHandler;
import com.bukoz.cryptoexchange.model.CryptoCurrency;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    private final CurrencyHandler currencyHandler;

    public CurrencyService(CurrencyHandler currencyHandler) {
        this.currencyHandler = currencyHandler;
    }

    public CryptoCurrency getCurrency(String name) {
        CryptoCurrency currency = currencyHandler.getByShortName(name.toUpperCase());
        if (currency == null) {
            currency = currencyHandler.getByLongName(name.toLowerCase());
        }
        if (currency == null) {
            throw new UnsupportedCurrencyException(name.toUpperCase() + " currency not found.");
        }
        return currency;
    }

}
