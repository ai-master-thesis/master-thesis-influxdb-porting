package it.e6h.influxdb.benchmark;

public class ItemProperty {
    private Long itemId;
    private String property;

    public ItemProperty(Long itemId, String property) {
        this.itemId = itemId;
        this.property = property;
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

    @Override
    public String toString() {
        return "ItemProperty{" +
                "itemId=" + itemId +
                ", property='" + property + '\'' +
                '}';
    }
}
