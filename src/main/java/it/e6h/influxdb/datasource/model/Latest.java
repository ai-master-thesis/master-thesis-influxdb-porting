package it.e6h.influxdb.datasource.model;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Objects;

public class Latest {
    private ObjectId id;
    private String value;
    private Date timestamp;

    public Latest() {
    }

    public Latest(ObjectId id, String value, Date timestamp) {
        this.id = id;
        this.value = value;
        this.timestamp = timestamp;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getString() {
        return timestamp;
    }

    public void setString(Date timestamp) {
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
