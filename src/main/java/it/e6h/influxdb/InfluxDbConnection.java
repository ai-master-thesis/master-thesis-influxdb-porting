package it.e6h.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;

public class InfluxDbConnection {
    private char[] token;
    private String bucket;
    private String org;
    private String url;

    public static InfluxDBClient connect(String url, char[] token, String bucket, String org) {
        InfluxDbConnection connection = new InfluxDbConnection();

        connection.setToken(token);
        connection.setBucket(bucket);
        connection.setOrg(org);
        connection.setUrl(url);

        return InfluxDBClientFactory.create(connection.getUrl(), connection.getToken(), connection.getOrg(), connection.getBucket());
    }

    public char[] getToken() {
        return token;
    }
    public void setToken(char[] token) {
        this.token = token;
    }

    public String getBucket() {
        return bucket;
    }
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getOrg() {
        return org;
    }
    public void setOrg(String org) {
        this.org = org;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
