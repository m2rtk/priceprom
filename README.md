# Prometheus stocks price quotes

Scrapes current price quotes from yahoo and exposes them for Prometheus

Reads comma-separated symbols from environment variable `SYMBOLS`

[Pre-Built Docker image](https://hub.docker.com/repository/docker/m2rtk/priceprom)

## Usage

```
$ docker run -it -e SYMBOLS=BTC-EUR,BTC-USD -p 8080:8080 m2rtk/priceprom
$ curl localhost:8080
price_quote{symbol="BTC-EUR"} 47430.97
price_quote{symbol="BTC-USD"} 54878.79
```