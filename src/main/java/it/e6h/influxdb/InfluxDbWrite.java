package it.e6h.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import it.e6h.influxdb.model.Latest;

import java.time.Instant;
import java.util.Date;

public class InfluxDbWrite {

    public static void write(InfluxDBClient influxClient) {
        try {
            WriteApiBlocking writeApi = influxClient.getWriteApiBlocking();

            //XXX mock
            Latest data = new Latest(123L, "value", Instant.now());

            writeApi.writeMeasurement(WritePrecision.MS, data);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
