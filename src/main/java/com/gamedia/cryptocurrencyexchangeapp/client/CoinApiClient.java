package com.gamedia.cryptocurrencyexchangeapp.client;

import com.gamedia.cryptocurrencyexchangeapp.config.CoinApiConfig;
import com.gamedia.cryptocurrencyexchangeapp.dto.response.CoinApiQuotesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class CoinApiClient {
    private final RestTemplate restTemplate;
    private final CoinApiConfig coinApiConfig;

    public CoinApiClient(RestTemplate restTemplate, CoinApiConfig coinApiConfig) {
        this.restTemplate = restTemplate;
        this.coinApiConfig = coinApiConfig;
    }

    public CoinApiQuotesResponse getQuoteList(String currency) {
        return restTemplate.getForObject(coinApiConfig.getApiUrl() + currency + "?" + coinApiConfig.getApiKey(),
                CoinApiQuotesResponse.class);
    }

    public CoinApiQuotesResponse getQuoteFiltered(String currency, List<String> filter) {
        String filteredCurrency;
        StringBuilder builder = new StringBuilder();
        String assetId = "filter_asset_id=";
        if (filter.size() > 1) {
            for (String quote : filter) {
                builder.append(quote).append(",");
            }
            filteredCurrency = builder.toString();
        } else {
            filteredCurrency = filter.get(0);
        }
        return restTemplate.getForObject(coinApiConfig.getApiUrl() + currency + "?" + assetId + filteredCurrency
                + "&" + coinApiConfig.getApiKey(), CoinApiQuotesResponse.class);
    }
}



