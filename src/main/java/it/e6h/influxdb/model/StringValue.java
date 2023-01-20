package it.e6h.influxdb.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name="string_value")
public class StringValue extends SensorData {
    @Column
    private String value;

    public StringValue(String bsonId, Long itemId, String property, Instant timestamp, String value) {
        super(bsonId, itemId, property, timestamp);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "StringValue{" +
                "bsonId='" + getBsonId() + '\'' +
                ", itemId='" + getItemId() + '\'' +
                ", property='" + getProperty() + '\'' +
                ", timestamp=" + getTimestamp() + '\'' +
                ", value=" + value +
                '}';
    }
}
