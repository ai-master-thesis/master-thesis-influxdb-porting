package it.e6h.influxdb.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;
import java.util.Objects;

@Measurement(name="latest")
public class Latest {
    @Column(tag = true)
    private Long id;
    @Column
    private String value;
    @Column(timestamp = true)
    private Instant timestamp;

    public Latest(Long id, String value, Instant timestamp) {
        this.id = id;
        this.value = value;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Instant getString() {
        return timestamp;
    }

    public void setString(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Latest{" +
                "id=" + id +
                ", value=" + value +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Latest latest = (Latest) o;
        return id.equals(latest.id) && Objects.equals(value, latest.value) && Objects.equals(timestamp, latest.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, timestamp);
    }
}
