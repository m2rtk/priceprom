package eu.m2rt.priceprom;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class PrometheusMetricsServer {

    private final HttpServer server;
    private final List<YahooQuote> quotes;

    public PrometheusMetricsServer(HttpServer server, List<YahooQuote> quotes) {
        this.server = server;
        this.quotes = quotes;
    }

    public static PrometheusMetricsServer newInstance(String... symbols) throws IOException {
        return new PrometheusMetricsServer(
                HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0),
                Arrays.stream(symbols)
                        .map(UrlYahooQuote::new)
                        .map(quote ->  new CachingYahooQuote(quote, Duration.ofSeconds(15)))
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public void start() {
        final var handler = new PrometheusMetricsHandler();
        server.createContext("/prometheus", handler);
        server.createContext("/", handler);
        server.start();
        System.out.println("Started server on " + server.getAddress());
    }

    private class PrometheusMetricsHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            final var body = new PrometheusMetricsBody(quotes).toString();
            exchange.sendResponseHeaders(200, body.length());

            try (final var out = exchange.getResponseBody()) {
                out.write(body.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
