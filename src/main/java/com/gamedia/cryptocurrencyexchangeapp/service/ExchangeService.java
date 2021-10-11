package com.gamedia.cryptocurrencyexchangeapp.service;

import com.gamedia.cryptocurrencyexchangeapp.config.CoinApiConfig;
import com.gamedia.cryptocurrencyexchangeapp.dto.request.ExchangeRequest;
import com.gamedia.cryptocurrencyexchangeapp.dto.response.CoinApiRateResponse;
import com.gamedia.cryptocurrencyexchangeapp.dto.response.CurrencyResponse;
import com.gamedia.cryptocurrencyexchangeapp.dto.response.ExchangeResponse;
import com.gamedia.cryptocurrencyexchangeapp.exception.HttpException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Log4j2
@Service
public class ExchangeService {
    private final RestTemplate restTemplate;
    private final CoinApiConfig apiConfig;

    public ExchangeService(RestTemplate restTemplate, CoinApiConfig apiConfig) {
        this.restTemplate = restTemplate;
        this.apiConfig = apiConfig;
    }

    public ExchangeResponse prepareExchange(ExchangeRequest request) {
        String from = request.getFrom();
        List<String> to = request.getTo();
        BigDecimal amount = request.getAmount();
        if (from == null || to == null || amount == null) {
            log.error(MessageFormat.format("Empty field.", HttpStatus.NOT_FOUND));
            throw new HttpException(("The field cannot be empty."), HttpStatus.NOT_FOUND);
        } else {
            List<CurrencyResponse> requestedRates = getRequestedRates(request);
            return createExchangeResponse(request, requestedRates);
        }
    }

    private List<CurrencyResponse> getRequestedRates(ExchangeRequest request) {
        List<String> currencies = request.getTo();
        return currencies.stream()
                .map(currency -> getRates(request, currency)).collect(toList());
    }

    private ExchangeResponse createExchangeResponse(ExchangeRequest request, List<CurrencyResponse> requestedRates) {
        ExchangeResponse exchangeResponse = new ExchangeResponse();
        exchangeResponse.setTo(requestedRates);
        exchangeResponse.setFrom(request.getFrom());
        return exchangeResponse;
    }

    public CurrencyResponse getRates(ExchangeRequest exchangeRequest, String currencyTo) {
        CoinApiRateResponse currency = restTemplate.getForObject(apiConfig.getApiUrl() + exchangeRequest.getFrom() + "/" + currencyTo + "?" + apiConfig.getApiKey(),
                CoinApiRateResponse.class);
        assert currency != null;
        return getCurrencyResponse(exchangeRequest, currencyTo, currency);
    }

    private CurrencyResponse getCurrencyResponse(ExchangeRequest exchangeRequest, String currencyTo, CoinApiRateResponse currency) {
        BigDecimal retrievedRate = currency.getRate().setScale(4, RoundingMode.DOWN);
        BigDecimal amount = exchangeRequest.getAmount().setScale(4, RoundingMode.DOWN);
        BigDecimal fee = countFee(retrievedRate, amount).setScale(4, RoundingMode.DOWN);
        BigDecimal resultNoFee = countWithoutFee(retrievedRate, amount);
        BigDecimal result = addFee(resultNoFee, fee).setScale(4, RoundingMode.DOWN);

        CurrencyResponse currencyResponse = new CurrencyResponse();
        currencyResponse.setCurrency(currencyTo);
        currencyResponse.setAmount(amount);
        currencyResponse.setRate(retrievedRate);
        currencyResponse.setFee(fee);
        currencyResponse.setResult(result);
        return currencyResponse;
    }

    private BigDecimal addFee(BigDecimal resultNoFee, BigDecimal fee) {
        return resultNoFee.add(fee);
    }

    private BigDecimal countFee(BigDecimal rate, BigDecimal amount) {
        BigDecimal resultNoFee = countWithoutFee(rate, amount);
        return resultNoFee.multiply(BigDecimal.valueOf(0.01));
    }

    private BigDecimal countWithoutFee(BigDecimal rate, BigDecimal amount) {
        return rate.multiply(amount);
    }
}
