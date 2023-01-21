package it.e6h.influxdb.model;

import it.e6h.influxdb.Constants;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;

public class Mapper {
    private static Logger logger = LoggerFactory.getLogger(Mapper.class);

    public static SensorData documentToInfluxPojo(Document doc) {
        SensorData sensorData = null;

        String bsonId = doc.getObjectId(Constants.BSON_ID_KEY).toString();
        Long itemId = doc.getLong(Constants.BSON_ITEM_ID_KEY);
        String property = doc.getString(Constants.BSON_PROPERTY_KEY);

        Instant timestamp = doc.getDate(Constants.BSON_TIMESTAMP_KEY).toInstant();

        String value = doc.getString(Constants.BSON_VALUE_KEY);
        String typeString = doc.getString(Constants.BSON_TYPE_KEY);
        ValueType type = ValueType.UNKNOWN;
        if(typeString != null)
            type = ValueType.valueOf(typeString);
        switch(type) {
            case NUMERIC:
                BigDecimal bdV = new BigDecimal(value);
                sensorData = new NumericValue(
                        bsonId,
                        itemId,
                        property,
                        timestamp,
                        bdV
                );
                break;
            case STRING:
                String stringV = value;
                sensorData = new StringValue(
                        bsonId,
                        itemId,
                        property,
                        timestamp,
                        stringV
                );
                break;
            case BOOLEAN:
                Boolean boolV = Boolean.parseBoolean(value);
                sensorData = new BooleanValue(
                        bsonId,
                        itemId,
                        property,
                        timestamp,
                        boolV
                );
                break;
            case UNKNOWN:
                logger.warn(Constants.LOG_MARKER, "Detected doc with unknown type: " + doc);
                String v = value;
                sensorData = new StringValue(
                        bsonId,
                        itemId,
                        property,
                        timestamp,
                        v
                );
                break;
        }

        return sensorData;
    }
}
