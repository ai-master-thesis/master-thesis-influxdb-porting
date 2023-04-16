package it.e6h.influxdb.util;

import java.util.Arrays;
import java.util.Optional;

public enum BenchmarkMenuItem {
    INVALID(0),
    INFORMATION_RETRIEVAL(1),
    MONGO_RESOURCE_CONSUMPTION(2),
    INFLUX_RESOURCE_CONSUMPTION(3);

    public final int numValue;

    BenchmarkMenuItem(int v) {
        this.numValue = v;
    }

    public static Optional<BenchmarkMenuItem> valueOf(int v) {
        return Arrays.stream(values())
                .filter(e -> e.numValue == v)
                .findFirst();
    }
}
