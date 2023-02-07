package it.e6h.influxdb.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.math.BigDecimal;
import java.time.Instant;

@Measurement(name="numeric_value")
public class NumericValue extends SensorData {
    @Column
    private BigDecimal value;

    public NumericValue(Long itemId, String property, Instant timestamp, BigDecimal value) {
        super(itemId, property, timestamp);
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NumericValue{" +
                "itemId='" + getItemId() + '\'' +
                ", property='" + getProperty() + '\'' +
                ", timestamp=" + getTimestamp() + '\'' +
                ", value=" + value +
                '}';
    }
}
