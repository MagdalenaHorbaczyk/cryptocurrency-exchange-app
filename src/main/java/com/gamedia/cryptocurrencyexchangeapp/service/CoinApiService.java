package com.gamedia.cryptocurrencyexchangeapp.service;

import com.gamedia.cryptocurrencyexchangeapp.client.CoinApiClient;
import com.gamedia.cryptocurrencyexchangeapp.dto.response.CoinApiQuotesResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class CoinApiService {
    private final CoinApiClient coinApiClient;

    public CoinApiService(CoinApiClient coinApiClient) {
        this.coinApiClient = coinApiClient;
    }

    public CoinApiQuotesResponse getQuotes(String baseCurrency, @RequestParam Optional<List<String>> filter) {
        CoinApiQuotesResponse quotesResult;
        if (filter.isPresent()) {
            quotesResult = coinApiClient.getQuoteFiltered(baseCurrency, filter.get());
        } else {
            quotesResult = coinApiClient.getQuoteList(baseCurrency);
        }
        return quotesResult;
    }
}
