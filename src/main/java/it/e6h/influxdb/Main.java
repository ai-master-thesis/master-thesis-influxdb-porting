package it.e6h.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import it.e6h.influxdb.benchmark.MyBenchmark;
import it.e6h.influxdb.datasource.MongoDbConnection;
import it.e6h.influxdb.datasource.MongoDbRead;
import it.e6h.influxdb.model.Mapper;
import it.e6h.influxdb.model.SensorData;
import org.bson.Document;
import org.openjdk.jmh.runner.RunnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDb;
    private static InfluxDBClient influxClient;

    public static void main(final String[] args) throws RunnerException {
        if(args.length != Constants.CL_ARGS_N) {
            logger.error(Constants.LOG_MARKER, "Missing required CL arguments");
        }

        switch(args[0]) {
            case "p":
                mongoClient = MongoDbConnection.connect(System.getProperty("mongodb.local.uri"));
                mongoDb = mongoClient.getDatabase(Constants.MONGO_DB_LOCAL);
                influxClient = InfluxDbConnection.connect(Constants.HOST, Constants.TOKEN, Constants.BUCKET_LATEST, Constants.ORG);
                porting();
                break;
            case "b":
                benchmarking(args);
                break;
            default:
                logger.error(Constants.LOG_MARKER, "Invalid CL arguments");
        }
    }

    private static void porting() {
        List<Document> mongoDocs = MongoDbRead.readAsDocument(mongoDb);
        logger.debug(Constants.LOG_MARKER, "Number of BSON documents = " + mongoDocs.size());

        //TODO Refactor invocation
//        MongoDbWrite.write(mongoClient, mongoDocs);

        List<SensorData> influxSeries = transformFromMongoToInflux(mongoDocs);
        logger.debug(Constants.LOG_MARKER, "Number of InfluxDB points = " + influxSeries.size());

        InfluxDbWrite.write(influxClient, influxSeries);
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

    //------------------------------------------------------------------------------------------------------------------

    private static void benchmarking(String[] args) throws RunnerException {
//XXX        org.openjdk.jmh.Main.main(args); //starts the benchmarking process
        MyBenchmark.run(); //starts the benchmarking process
    }

}
