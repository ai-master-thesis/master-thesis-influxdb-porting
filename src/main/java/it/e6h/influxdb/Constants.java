package it.e6h.influxdb;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Constants {
    public static final String LOG_MARKER_STRING = "E6H";
    public static final Marker LOG_MARKER = MarkerFactory.getMarker(LOG_MARKER_STRING);

    public static final char[] TOKEN = "XVmIGzd8vvXlPvIuDD6KXYMo-UOGoWd8yOYBW2HhwOQ3Bhoq6_8Krs-1ix-lWH38_Cs4uSglALamXkZETJXjgg==".toCharArray();
    public static final String ORG = "E6H";
    public static final String BUCKET = "sensor_data_52";

    public static final String HOST = "http://localhost:8086";

    public static final String BSON_ID_KEY = "_id";
    public static final Object BSON_ITEM_ID_KEY = "itemId";
    public static final Object BSON_PROPERTY_KEY = "property";
    public static final String BSON_TIMESTAMP_KEY = "timestamp";
    public static final Object BSON_TYPE_KEY = "type";
    public static final String BSON_VALUE_KEY = "value";

    public static final String TARGET_GROUP = "52";

}
