package it.e6h.influxdb.model;

import it.e6h.influxdb.Constants;
import org.bson.Document;

import java.time.Instant;

public class Mapper {

    public static Latest documentToInfluxPojo(Document doc) {
        String id = doc.getObjectId(Constants.BSON_ID_KEY).toString();
        String value = doc.getString(Constants.BSON_VALUE_KEY);
        Instant timestamp = doc.getDate(Constants.BSON_TIMESTAMP_KEY).toInstant();

        return new Latest(id, value, timestamp);
    }
}
