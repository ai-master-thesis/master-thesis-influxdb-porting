package it.e6h.influxdb.model;

import it.e6h.influxdb.Constants;
import org.bson.Document;

import java.math.BigDecimal;
import java.time.Instant;

public class Mapper {

    public static SensorData documentToInfluxPojo(Document doc) {
        SensorData sensorData = null;

        String bsonId = doc.getObjectId(Constants.BSON_ID_KEY).toString();
        Long itemId = doc.getLong(Constants.BSON_ITEM_ID_KEY);
        String property = doc.getString(Constants.BSON_PROPERTY_KEY);

        Instant timestamp = doc.getDate(Constants.BSON_TIMESTAMP_KEY).toInstant();

        String value = doc.getString(Constants.BSON_VALUE_KEY);
        ValueType type = ValueType.valueOf(doc.getString(Constants.BSON_TYPE_KEY));
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
        }

        return sensorData;
    }
}
