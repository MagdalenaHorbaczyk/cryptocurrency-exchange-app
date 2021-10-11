package com.gamedia.cryptocurrencyexchangeapp.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyResponse {
    private String currency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal result;
    private BigDecimal fee;
}
