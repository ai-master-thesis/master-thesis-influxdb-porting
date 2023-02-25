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

    public static List<FluxRecord> getLatest(InfluxDBClient influxClient) {
        try {
            String flux = new String(
                    InfluxDbRead.class.getClassLoader().getResourceAsStream("query_latest.flux").readAllBytes());
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

}
