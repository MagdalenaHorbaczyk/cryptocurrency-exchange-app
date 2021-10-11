## cryptocurrency-exchange-app

Simple Spring Boot app allowing to:
- get a list of quotes for a given cryptocurrency,
- forecast the exchange of cryptocurrency in the specified amount at the current rate. 
  (During the calculation a commission of 1% is inclusive).

## Requirements

- JDK 8 or higher
- Gradle

## Testing the endpoints

Endpoints can be tested by Postman. 
For the convenience there is a Postman collection file added in the root directory.

## Example request

-    currencies/{baseCurrency}

localhost:8080/currencies/BTC?filter=ADC,AZU,AIV

localhost:8080/currencies/BTC?filter=USD

localhost:8080/currencies/BTC

-    currencies/exchange

RequestBody{ "from":"BTC", "to":["ETH","USDT"], "amount": 1000 }



