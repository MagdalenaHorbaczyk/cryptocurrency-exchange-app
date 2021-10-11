package com.gamedia.cryptocurrencyexchangeapp.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CoinApiQuotesResponse {
    @JsonAlias("asset_id_base")
    private String source;
    @JsonProperty("rates")
    private List<CoinApiRateResponse> rates;
}
