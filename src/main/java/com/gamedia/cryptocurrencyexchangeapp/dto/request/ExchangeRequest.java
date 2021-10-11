package com.gamedia.cryptocurrencyexchangeapp.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ExchangeRequest {
    @NotEmpty(message = "Please provide a currency")
    private String from;
    @NotEmpty(message = "Please provide a currency")
    private List<String> to;
    @NotEmpty(message = "Please provide an amount")
    private BigDecimal amount;
}
