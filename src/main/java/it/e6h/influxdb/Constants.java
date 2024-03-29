package it.e6h.influxdb;

import it.e6h.influxdb.util.Util;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.options.TimeValue;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Constants {
    private static Properties secret = Util.loadPropertiesFromClasspath("secret.properties");

    public static final int CL_ARGS_N = 1;

    public static final String LOG_MARKER_STRING = "E6H";
    public static final Marker LOG_MARKER = MarkerFactory.getMarker(LOG_MARKER_STRING);

    public static final String TARGET_GROUP = "52";
    public static final Long TARGET_ITEM_ID = 1233L;
    public static final String TARGET_PROPERTY = "Photocell";
    public static final Long TARGET_PROPERTY_ID = 5L;
    public static final int TARGET_RECORDS_N = 100;

    // MongoDB

    public static final String MONGO_REMOTE_CON_STR = secret.getProperty("mongodb.remote.uri");
    public static final String MONGO_LOCAL_CON_STR = secret.getProperty("mongodb.local.uri");
    public static final String MONGO_DB_REMOTE = "smactory-telemetry";
    public static final String BSON_ID_KEY = "_id";
    public static final String BSON_ITEM_ID_KEY = "itemId";
    public static final String BSON_PROPERTY_KEY = "property";
    public static final String BSON_TIMESTAMP_KEY = "timestamp";
    public static final String BSON_TYPE_KEY = "type";
    public static final String BSON_VALUE_KEY = "value";

    public static final String MONGO_DB_LOCAL = "smc-telemetry";

    // InfluxDB

    public static final char[] TOKEN = secret.getProperty("influxdb.local.token").toCharArray();
    public static final String ORG = "E6H";
    public static final String BUCKET = "sensor_data_52";
    public static final String BUCKET_EX = "ex";
    public static final String BUCKET_FILTER = "sensor_data_52_1233_Photocell";
    public static final String BUCKET_LATEST = "latest_52_1233_5";

    public static final String HOST = "http://localhost:8086";

    // Benchmark

    public static final class InfRetrievalBenchmarkParams {
        public static final int FORKS = 1;
        public static final int THREADS = 1;
        public static final Mode MODE = Mode.AverageTime;
        public static final int WARMUP_ITERATIONS = 1;
        public static final TimeValue WARMUP_TIME = new TimeValue(10L, TimeUnit.SECONDS);
        public static final int MEASUREMENT_ITERATIONS = 3;
        public static final TimeValue MEASUREMENT_TIME = new TimeValue(20L, TimeUnit.SECONDS);;
        public static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
    }
    public static final class ResConsumptionBenchmarkParams {
        public static final int FORKS = 1;
        public static final int THREADS = 1;
        public static final Mode MODE = Mode.Throughput;
        public static final int WARMUP_ITERATIONS = 1;
        public static final TimeValue WARMUP_TIME = new TimeValue(30L, TimeUnit.SECONDS);
        public static final int MEASUREMENT_ITERATIONS = 3;
        public static final TimeValue MEASUREMENT_TIME = new TimeValue(90L, TimeUnit.SECONDS);;
        public static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
    }

}
