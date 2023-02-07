package it.e6h.influxdb.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name="boolean_value")
public class BooleanValue extends SensorData {
    @Column
    private Boolean value;

    public BooleanValue(Long itemId, String property, Instant timestamp, Boolean value) {
        super(itemId, property, timestamp);
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BooleanValue{" +
                "itemId='" + getItemId() + '\'' +
                ", property='" + getProperty() + '\'' +
                ", timestamp=" + getTimestamp() + '\'' +
                ", value=" + value +
                '}';
    }
}
