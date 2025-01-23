package com.bukoz.cryptoexchange.service;

import com.bukoz.cryptoexchange.domain.CryptoCurrency;
import com.bukoz.cryptoexchange.exception.internal.UnsupportedCurrencyException;
import com.bukoz.cryptoexchange.externalapi.common.CurrencyHandler;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CryptoCurrency> getAllCurrencies() {
        return currencyHandler.getAllCurrencies();
    }

}
