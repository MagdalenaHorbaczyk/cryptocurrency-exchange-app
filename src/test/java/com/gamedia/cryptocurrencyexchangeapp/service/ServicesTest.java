package com.gamedia.cryptocurrencyexchangeapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamedia.cryptocurrencyexchangeapp.CryptocurrencyExchangeApplication;
import com.gamedia.cryptocurrencyexchangeapp.config.CoinApiConfig;
import com.gamedia.cryptocurrencyexchangeapp.dto.request.ExchangeRequest;
import com.gamedia.cryptocurrencyexchangeapp.dto.response.CoinApiQuotesResponse;
import com.gamedia.cryptocurrencyexchangeapp.dto.response.CoinApiRateResponse;
import com.gamedia.cryptocurrencyexchangeapp.dto.response.CurrencyResponse;
import com.gamedia.cryptocurrencyexchangeapp.dto.response.ExchangeResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CryptocurrencyExchangeApplication.class)
public class ServicesTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CoinApiConfig coinApiConfig;
    @Autowired
    private CoinApiService coinApiService;
    @Autowired
    private ExchangeService exchangeService;
    private MockRestServiceServer mockServer;

    @Before
    public void init() {
        mockServer = MockRestServiceServer.bindTo(this.restTemplate)
                .ignoreExpectOrder(true).build();
    }

    @Test
    public void getQuotesTest() throws JsonProcessingException, URISyntaxException {
        //given
        String baseCurrency = "BTC";
        String assetId = "filter_asset_id=";
        String filteredCurrencies = "ETH,USDT,";

        List<String> filter = Arrays.asList("ETH", "USDT");
        CoinApiQuotesResponse quotesResponse = new CoinApiQuotesResponse();
        quotesResponse.setSource("BTC");
        List<CoinApiRateResponse> listOfCurrencies = new ArrayList<>();
        CoinApiRateResponse rate = new CoinApiRateResponse();
        rate.setToCurrency("ETH");
        rate.setRate(BigDecimal.valueOf(15.8));
        listOfCurrencies.add(rate);
        CoinApiRateResponse rate1 = new CoinApiRateResponse();
        rate1.setToCurrency("USDT");
        rate1.setRate(BigDecimal.valueOf(54900));
        listOfCurrencies.add(rate1);
        quotesResponse.setRates(listOfCurrencies);

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(coinApiConfig.getApiUrl() + baseCurrency + "?" +
                        assetId + filteredCurrencies + "&" + coinApiConfig.getApiKey())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(quotesResponse))
                );
        //when
        Optional<CoinApiQuotesResponse> result = Optional.ofNullable(coinApiService.getQuotes(baseCurrency, Optional.of(filter)));
        //then
        mockServer.verify();
        Assert.assertEquals(quotesResponse.getSource(), result.get().getSource());
        Assert.assertEquals(quotesResponse.getRates().get(0).getToCurrency(), result.get().getRates().get(0).getToCurrency());
        Assert.assertEquals(quotesResponse.getRates().get(1).getToCurrency(), result.get().getRates().get(1).getToCurrency());
    }

    @Test
    public void prepareExchangeTest() throws JsonProcessingException, URISyntaxException {
        //given
        ExchangeRequest request = new ExchangeRequest();
        List<String> currencyTo = Arrays.asList("USDT", "ETH");
        request.setFrom("BTC");
        request.setAmount(BigDecimal.valueOf(100.0));
        request.setTo(currencyTo);
        CoinApiRateResponse rate = new CoinApiRateResponse();
        rate.setToCurrency("USDT");
        rate.setRate(BigDecimal.valueOf(100.0));
        CoinApiRateResponse rate1 = new CoinApiRateResponse();
        rate1.setToCurrency("ETH");
        rate1.setRate(BigDecimal.valueOf(200.0));

        ExchangeResponse response = new ExchangeResponse();
        response.setFrom(request.getFrom());
        List<CurrencyResponse> rates = new ArrayList<>();
        CurrencyResponse currencyResponse = new CurrencyResponse();
        currencyResponse.setAmount(BigDecimal.valueOf(100.00).setScale(4, RoundingMode.DOWN));
        currencyResponse.setCurrency("USDT");
        currencyResponse.setFee(BigDecimal.valueOf(100.0).setScale(4, RoundingMode.DOWN));
        currencyResponse.setRate(BigDecimal.valueOf(100.0).setScale(4, RoundingMode.DOWN));
        currencyResponse.setResult(BigDecimal.valueOf(10100.0).setScale(4, RoundingMode.DOWN));
        rates.add(currencyResponse);

        CurrencyResponse currencyResponse1 = new CurrencyResponse();
        currencyResponse1.setAmount(BigDecimal.valueOf(100.0).setScale(4, RoundingMode.DOWN));
        currencyResponse1.setCurrency("ETH");
        currencyResponse1.setFee(BigDecimal.valueOf(200.0).setScale(4, RoundingMode.DOWN));
        currencyResponse1.setRate(BigDecimal.valueOf(200.0).setScale(4, RoundingMode.DOWN));
        currencyResponse1.setResult(BigDecimal.valueOf(20200.0).setScale(4, RoundingMode.DOWN));
        rates.add(currencyResponse1);

        response.setTo(rates);

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(coinApiConfig.getApiUrl() + request.getFrom() + "/" +
                        currencyTo.get(0) + "?" + coinApiConfig.getApiKey())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(rate))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(coinApiConfig.getApiUrl() + request.getFrom() + "/" +
                        currencyTo.get(1) + "?" + coinApiConfig.getApiKey())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(rate1))
                );
        //when
        ExchangeResponse result = exchangeService.prepareExchange(request);
        mockServer.verify();
        mockServer.verify();
        //then
        Assert.assertEquals(response.getTo().get(0).getAmount(), result.getTo().get(0).getAmount());
        Assert.assertEquals(response.getTo().get(0).getFee(), result.getTo().get(0).getFee());
        Assert.assertEquals(response.getTo().get(0).getRate(), result.getTo().get(0).getRate());
        Assert.assertEquals(response.getTo().get(0).getResult(), result.getTo().get(0).getResult());
        Assert.assertEquals(response.getTo().get(1).getAmount(), result.getTo().get(1).getAmount());
        Assert.assertEquals(response.getTo().get(1).getFee(), result.getTo().get(1).getFee());
        Assert.assertEquals(response.getTo().get(1).getRate(), result.getTo().get(1).getRate());
        Assert.assertEquals(response.getTo().get(1).getResult(), result.getTo().get(1).getResult());
    }
}
