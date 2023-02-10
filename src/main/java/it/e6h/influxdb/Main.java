package it.e6h.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.mongodb.client.MongoClient;
import it.e6h.influxdb.datasource.MongoDbConnection;
import it.e6h.influxdb.datasource.MongoDbRead;
import it.e6h.influxdb.model.Mapper;
import it.e6h.influxdb.model.SensorData;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        MongoClient mongoClient = MongoDbConnection.connect();
        List<Document> mongoDocs = MongoDbRead.readAsDocument(mongoClient);
        logger.debug(Constants.LOG_MARKER, "Number of BSON documents = " + mongoDocs.size());

        List<SensorData> influxSeries = new ArrayList<>();

        for(Document doc: mongoDocs) {
            logger.debug("BSON = " + doc);

            SensorData point = Mapper.documentToInfluxPojo(doc);
            influxSeries.add(point);
        }

        logger.debug(Constants.LOG_MARKER, "Number of InfluxDB points = " + influxSeries.size());

        InfluxDBClient influxClient = InfluxDbConnection.connect(Constants.HOST, Constants.TOKEN, Constants.BUCKET_FILTER, Constants.ORG);
        InfluxDbWrite.write(influxClient, influxSeries);
    }

}
