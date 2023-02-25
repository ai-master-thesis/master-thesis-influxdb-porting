package it.e6h.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.events.AbstractWriteEvent;
import com.influxdb.client.write.events.EventListener;
import it.e6h.influxdb.model.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InfluxDbWrite {
    private static Logger logger = LoggerFactory.getLogger(InfluxDbWrite.class);

    public static void write(InfluxDBClient influxClient, List<SensorData> influxSeries) {
        try {
            // Example with blocking API
//            WriteApiBlocking writeApi = influxClient.getWriteApiBlocking();
//            Latest data = new Latest("123", "value", Instant.now());
//            writeApi.writeMeasurement(WritePrecision.MS, data);

            // Asynchronous API
            WriteApi writeApi = influxClient.makeWriteApi(
                    WriteOptions.builder().build()
            );

            EventListener<AbstractWriteEvent> listener = new InfluxDbEventListener();
            writeApi.listenEvents(AbstractWriteEvent.class, listener);

            writeApi.writeMeasurements(WritePrecision.MS, influxSeries);

            synchronized(listener) {
                listener.wait();
            }

        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
