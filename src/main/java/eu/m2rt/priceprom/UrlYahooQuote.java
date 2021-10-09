package eu.m2rt.priceprom;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public final class UrlYahooQuote implements YahooQuote {

    private static final Pattern priceRegex = Pattern.compile("regularMarketPrice\":(.*?),");

    private final String symbol;
    private final String urlTemplate;

    public UrlYahooQuote(String symbol) {
        this.symbol = symbol;
        this.urlTemplate = "https://query1.finance.yahoo.com/v8/finance/chart/" + symbol + "?symbol=" + symbol + "&period1=%d&period2=%d";
    }

    @Override
    public double query() {
        return query(System.currentTimeMillis());
    }

    public double query(long epochTimeMillis) {
        try {
            final var epochTimeSeconds = epochTimeMillis / 1000;
            final var url = new URL(String.format(urlTemplate, epochTimeSeconds - 100, epochTimeSeconds));
            System.out.printf(
                    "%s - Fetching %s\n",
                    DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()),
                    url
            );
            try (final var in = url.openStream()) {
                final var body = new String(in.readAllBytes());

                final var matcher = priceRegex.matcher(body);
                if (matcher.find()) {
                    return Double.parseDouble(matcher.group(1));
                } else {
                    throw new RuntimeException("Could not find regularMarketPrice. URL=" + url + " body=" + body);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String symbol() {
        return symbol;
    }
}
