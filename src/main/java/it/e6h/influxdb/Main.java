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
        List<Document> mongoDocs = readFromRemoteMongoDb();
        logger.debug(Constants.LOG_MARKER, "Number of BSON documents = " + mongoDocs.size());

        writeToLocalMongoDb(mongoDocs);

        //XXX
//        List<SensorData> influxSeries = transformFromMongoToInflux(mongoDocs);
//        logger.debug(Constants.LOG_MARKER, "Number of InfluxDB points = " + influxSeries.size());
//
//        writeToLocalInfluxDb(influxSeries);
    }

    private static List<Document> readFromRemoteMongoDb() {
        MongoClient mongoClient = MongoDbConnection.connect(System.getProperty("mongodb.remote.uri"));
        return MongoDbRead.readAsDocument(mongoClient);
    }

    private static List<SensorData> transformFromMongoToInflux(List<Document> mongoDocs) {
        List<SensorData> influxSeries = new ArrayList<>();

        for(Document doc: mongoDocs) {
            logger.debug("BSON = " + doc);

            SensorData point = Mapper.documentToInfluxPojo(doc);
            influxSeries.add(point);
        }

        return influxSeries;
    }

    private static void writeToLocalInfluxDb(List<SensorData> influxSeries) {
        InfluxDBClient influxClient = InfluxDbConnection.connect(Constants.HOST, Constants.TOKEN, Constants.BUCKET_FILTER, Constants.ORG);
        InfluxDbWrite.write(influxClient, influxSeries);
    }

    private static void writeToLocalMongoDb(List<Document> mongoDocs) {
        MongoClient mongoClient = MongoDbConnection.connect(System.getProperty("mongodb.local.uri"));
        MongoDbWrite.write(mongoClient, mongoDocs);
    }

}
