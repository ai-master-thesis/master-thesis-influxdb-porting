package it.e6h.influxdb.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;
import java.util.Objects;

@Measurement(name="sensor_data")
public abstract class SensorData {
    @Column(tag = true)
    private Long itemId;
    @Column(tag = true)
    private String property;

    @Column(timestamp = true)
    private Instant timestamp;

    public SensorData(Long itemId, String property, Instant timestamp) {
        this.itemId = itemId;
        this.property = property;
        this.timestamp = timestamp;
    }

    public Long getItemId() {
        return itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getProperty() {
        return property;
    }
    public void setProperty(String property) {
        this.property = property;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorData that = (SensorData) o;
        return itemId.equals(that.itemId) && property.equals(that.property);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, property);
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "itemId='" + itemId + '\'' +
                ", property='" + property + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
