package com.bukoz.cryptoexchange.externalapi.coingecko;

import java.util.List;

public class CoinGeckoPriceApiHelper {

    private CoinGeckoPriceApiHelper() {
    }

    public static String buildPriceApiUrl(String currency, List<String> filters, String apiUrl) {
        if (currency == null || currency.isEmpty()) {
            throw new IllegalArgumentException("Currency value should not be empty in external API URL");
        }
        String filterString = filters.isEmpty() ? "" : String.join(",", filters).toLowerCase();
        return String.format("%s?ids=%s&vs_currencies=%s", apiUrl, currency, filterString);
    }
}
