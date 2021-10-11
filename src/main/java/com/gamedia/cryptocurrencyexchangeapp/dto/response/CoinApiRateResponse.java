package com.gamedia.cryptocurrencyexchangeapp.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinApiRateResponse {
    @JsonProperty("asset_id_base")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fromCurrency;
    @JsonAlias("asset_id_quote")
    private String toCurrency;
    @JsonProperty("rate")
    private BigDecimal rate;
}
