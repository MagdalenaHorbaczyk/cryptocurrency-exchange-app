package com.gamedia.cryptocurrencyexchangeapp.controller;

import com.gamedia.cryptocurrencyexchangeapp.dto.request.ExchangeRequest;
import com.gamedia.cryptocurrencyexchangeapp.dto.response.ExchangeResponse;
import com.gamedia.cryptocurrencyexchangeapp.service.ExchangeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/currencies")
public class ExchangeController {
    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping(value = "/exchange")
    public ExchangeResponse prepareExchange(@Valid @RequestBody ExchangeRequest request) {
        return exchangeService.prepareExchange(request);
    }
}
