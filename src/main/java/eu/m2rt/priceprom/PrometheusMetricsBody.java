package eu.m2rt.priceprom;

import java.util.List;

public final class PrometheusMetricsBody {

    private final List<YahooQuote> quotes;

    public PrometheusMetricsBody(List<YahooQuote> quotes) {
        this.quotes = quotes;
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();

        for (final var quote : quotes) {
            sb
                    .append("price_quote")
                    .append("{symbol=\"")
                    .append(quote.symbol())
                    .append("\"} ")
                    .append(quote.query())
                    .append("\n");
        }

        return sb.toString();
    }
}
