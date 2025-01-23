package com.bukoz.cryptoexchange.handler.external;

import com.bukoz.cryptoexchange.handler.CurrencyHandler;
import com.bukoz.cryptoexchange.model.CryptoCurrency;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CoinGeckoCurrencyHandler implements CurrencyHandler {

    private final Map<String, CryptoCurrency> shortNameMap = new HashMap<>();
    private final Map<String, CryptoCurrency> longNameMap = new HashMap<>();

    public CoinGeckoCurrencyHandler() {
        addCurrency("BTC", "bitcoin");
        addCurrency("ETH", "ethereum");
        addCurrency("LTC", "litecoin");
        addCurrency("BCH", "bitcoin-cash");
        addCurrency("BNB", "binancecoin");
        addCurrency("EOS", "eos");
        addCurrency("XRP", "xrp");
        addCurrency("XLM", "stellar");
        addCurrency("LINK", "chainlink");
        addCurrency("DOT", "polkadot");
        addCurrency("YFI", "yearn-finance");
    }

    private void addCurrency(String shortName, String longName) {
        CryptoCurrency currency = new CryptoCurrency(shortName, longName);
        shortNameMap.put(shortName, currency);
        longNameMap.put(longName, currency);
    }

    @Override
    public CryptoCurrency getByShortName(String shortName) {
        return shortNameMap.get(shortName);
    }

    @Override
    public CryptoCurrency getByLongName(String longName) {
        return longNameMap.get(longName);
    }

    @Override
    public List<CryptoCurrency> getAllCurrencies() {
        return new ArrayList<>(shortNameMap.values());
    }
}
