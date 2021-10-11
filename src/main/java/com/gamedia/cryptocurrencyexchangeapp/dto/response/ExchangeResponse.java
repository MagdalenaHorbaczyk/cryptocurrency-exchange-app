package com.gamedia.cryptocurrencyexchangeapp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExchangeResponse {
    @JsonProperty("from")
    private String from;
    @JsonProperty("to")
    private List<CurrencyResponse> to;
}
