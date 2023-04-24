package it.e6h.influxdb.util;

import java.util.Arrays;
import java.util.Optional;

public enum AppMode {
    INVALID,
    PORT_MONGO_INFLUX,
    PORT_MONGO_MONGO,
    BENCHMARK_INF_RET,
    BENCHMARK_INF_RET_RAND,
    BENCHMARK_RES_CONS_MONGO,
    BENCHMARK_RES_CONS_INFLUX;
}
