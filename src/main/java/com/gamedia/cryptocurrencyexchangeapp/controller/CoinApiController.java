package com.gamedia.cryptocurrencyexchangeapp.controller;

import com.gamedia.cryptocurrencyexchangeapp.dto.response.CoinApiQuotesResponse;
import com.gamedia.cryptocurrencyexchangeapp.service.CoinApiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/currencies")
public class CoinApiController {
    private final CoinApiService coinApiService;

    public CoinApiController(CoinApiService coinApiService) {
        this.coinApiService = coinApiService;
    }

    @GetMapping(value = "/{baseCurrency}")
    public CoinApiQuotesResponse getQuotes(@PathVariable("baseCurrency") String baseCurrency, @RequestParam Optional<List<String>> filter) {
        return coinApiService.getQuotes(baseCurrency, filter);
    }
}
