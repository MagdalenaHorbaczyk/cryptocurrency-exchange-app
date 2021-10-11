package com.gamedia.cryptocurrencyexchangeapp.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CoinApiConfig {
    //    @Value("${coinApi.endpoint.prod}") // (It doesn't work while testing - to later investigation)
    @Value("https://rest.coinapi.io/v1/exchangerate/")
    private String apiUrl;
    //    @Value("${coinApi.key}") // (It doesn't work while testing - to later investigation)
    @Value("apikey=6E874441-FFFC-4CE4-BE1B-A5297878F5C4")
    private String apiKey;
}
