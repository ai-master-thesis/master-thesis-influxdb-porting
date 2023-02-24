package it.e6h.influxdb;

import org.openjdk.jmh.annotations.Mode;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.concurrent.TimeUnit;

public class Constants {
    public static final String LOG_MARKER_STRING = "E6H";
    public static final Marker LOG_MARKER = MarkerFactory.getMarker(LOG_MARKER_STRING);

    public static final char[] TOKEN = "XVmIGzd8vvXlPvIuDD6KXYMo-UOGoWd8yOYBW2HhwOQ3Bhoq6_8Krs-1ix-lWH38_Cs4uSglALamXkZETJXjgg==".toCharArray();
    public static final String ORG = "E6H";
    public static final String BUCKET = "sensor_data_52";
    public static final String BUCKET_EX = "ex";
    public static final String BUCKET_FILTER = "sensor_data_52_1233_Photocell";

    public static final String HOST = "http://localhost:8086";

    public static final String MONGO_DB_REMOTE = "smactory-telemetry";
    public static final String BSON_ID_KEY = "_id";
    public static final String BSON_ITEM_ID_KEY = "itemId";
    public static final String BSON_PROPERTY_KEY = "property";
    public static final String BSON_TIMESTAMP_KEY = "timestamp";
    public static final String BSON_TYPE_KEY = "type";
    public static final String BSON_VALUE_KEY = "value";

    public static final String MONGO_DB_LOCAL = "smc-telemetry";

    public static final String TARGET_GROUP = "52";
    public static final Long TARGET_ITEM_ID = 1233L;
    public static final String TARGET_PROPERTY = "Photocell";
    public static final Long TARGET_PROPERTY_ID = 5L;

    // Benchmark
    public static final class BenchmarkParams {
        public static final int FORKS = 1;
        public static final int THREADS = 1;
        public static final Mode MODE = Mode.SingleShotTime;
        public static final int WARMUP_ITERATIONS = 1;
        public static final int WARMUP_BATCH_SIZE = 10;
        public static final int MEASUREMENT_ITERATIONS = 3;
        public static final int MEASUREMENT_BATCH_SIZE = 5;
        public static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
    }

}
