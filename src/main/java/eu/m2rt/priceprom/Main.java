package eu.m2rt.priceprom;

import java.io.IOException;

import static java.lang.System.getenv;

public final class Main {

    public static void main(String[] args) throws IOException {
        final var symbols = getListFromEnv("SYMBOLS");

        PrometheusMetricsServer.newInstance(symbols).start();
    }

    private static String[] getListFromEnv(String name) {
        final var value = getenv(name);

        if (value == null) {
            return new String[0];
        }

        return value.split(",\\s*");
    }
}
