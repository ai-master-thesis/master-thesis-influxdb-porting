package it.e6h.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class InfluxDbRead {
    private static Logger logger = LoggerFactory.getLogger(InfluxDbRead.class);

    public static List<FluxRecord> getAll(InfluxDBClient influxClient, String bucketName) {
        try {
            String flux = new String(
                    InfluxDbRead.class.getClassLoader().getResourceAsStream("query_all.flux").readAllBytes());
            flux = String.format(flux, bucketName);

            QueryApi queryApi = influxClient.getQueryApi();

            List<FluxTable> tables = queryApi.query(flux);

            List<FluxRecord> records = new ArrayList<>();
            for (FluxTable fluxTable : tables)
                records.addAll(fluxTable.getRecords());

            return records;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<FluxRecord> getTopByItemIdAndProperty(InfluxDBClient influxClient, String bucketName) {
        try {
            String flux = new String(InfluxDbRead.class.getClassLoader().getResourceAsStream(
                    "query_top_byItemIdAndProperty.flux").readAllBytes());
            flux = String.format(flux, bucketName,
                    Constants.TARGET_ITEM_ID, Constants.TARGET_PROPERTY, Constants.TARGET_RECORDS_N);

            QueryApi queryApi = influxClient.getQueryApi();

            List<FluxTable> tables = queryApi.query(flux);

            List<FluxRecord> records = new ArrayList<>();
            for (FluxTable fluxTable : tables)
                records.addAll(fluxTable.getRecords());

            return records;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<FluxRecord> getTopByItemIdAndPropertyRC(InfluxDBClient client, String bucketName,
                                                               Long itemId, String property) {
        try {
            String flux = new String(InfluxDbRead.class.getClassLoader().getResourceAsStream(
                    "query_top_byItemIdAndProperty_rc.flux").readAllBytes());
            flux = String.format(flux, bucketName,
                    itemId, property, Constants.TARGET_RECORDS_N);

            QueryApi queryApi = client.getQueryApi();

            List<FluxTable> tables = queryApi.query(flux);

            List<FluxRecord> records = new ArrayList<>();
            for (FluxTable fluxTable : tables)
                records.addAll(fluxTable.getRecords());

            return records;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
