package it.e6h.influxdb.benchmark;

import java.util.List;

public class ItemProperties {
    private Long itemId;
    private List<String> properties;

    public Long getItemId() {
        return itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public List<String> getProperties() {
        return properties;
    }
    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "ItemProperties{" +
                "itemId=" + itemId +
                ", properties=" + properties +
                '}';
    }
}
