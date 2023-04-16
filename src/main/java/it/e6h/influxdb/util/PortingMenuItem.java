package it.e6h.influxdb.util;

import java.util.Arrays;
import java.util.Optional;

public enum PortingMenuItem {
    INVALID(0),
    REMOTE_MONGODB(1),
    LOCAL_MONGODB(2),
    LOCAL_INFLUXDB(3);

    public final int numValue;

    PortingMenuItem(int v) {
        this.numValue = v;
    }

    public static Optional<PortingMenuItem> valueOf(int v) {
        return Arrays.stream(values())
                .filter(e -> e.numValue == v)
                .findFirst();
    }
}
