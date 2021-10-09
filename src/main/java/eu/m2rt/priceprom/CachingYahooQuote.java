package eu.m2rt.priceprom;

import java.time.Duration;
import java.time.Instant;

public final class CachingYahooQuote implements YahooQuote {

    private final YahooQuote delegate;
    private final Duration ttl;

    private Double lastResult;
    private Instant lastQueryTime;

    public CachingYahooQuote(YahooQuote delegate, Duration ttl) {
        this.delegate = delegate;
        this.ttl = ttl;
        this.lastResult = null;
        this.lastQueryTime = Instant.EPOCH;
    }

    @Override
    public String symbol() {
        return delegate.symbol();
    }

    @Override
    public double query() {
        final var now = Instant.now();

        if (lastResult == null || now.isAfter(lastQueryTime.plus(ttl))) {
            this.lastResult = delegate.query();
            this.lastQueryTime = now;
        }

        return lastResult;
    }
}
