package it.e6h.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import it.e6h.influxdb.benchmark.InfRetrievalBenchmark;
import it.e6h.influxdb.benchmark.InfluxResConsumptionBenchmark;
import it.e6h.influxdb.benchmark.MongoResConsumptionBenchmark;
import it.e6h.influxdb.datasource.MongoDbConnection;
import it.e6h.influxdb.datasource.MongoDbRead;
import it.e6h.influxdb.model.Mapper;
import it.e6h.influxdb.model.SensorData;
import it.e6h.influxdb.util.AppMode;
import it.e6h.influxdb.util.BenchmarkMenuItem;
import it.e6h.influxdb.util.PortingMenuItem;
import org.bson.Document;
import org.openjdk.jmh.runner.RunnerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static it.e6h.influxdb.util.PortingMenuItem.*;
import static it.e6h.influxdb.util.AppMode.*;
import static it.e6h.influxdb.util.Util.*;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static MongoDatabase mongoSource;
    private static MongoDatabase mongoDestination;

    public static void main(final String[] args) throws RunnerException {
        if(args.length != Constants.CL_ARGS_N) {
            logger.error(Constants.LOG_MARKER, "Missing required CL arguments");
        }
        AppMode appMode = menuInteraction(args[0]);

        switch(appMode) {
            case PORT_MONGO_INFLUX:
                portingFromMongoToInflux();
                break;
            case PORT_MONGO_MONGO:
                //TODO
                break;
            case BENCHMARK_INF_RET:
                InfRetrievalBenchmark.run();
                break;
            case BENCHMARK_RES_CONS_MONGO:
                MongoResConsumptionBenchmark.run();
                break;
            case BENCHMARK_RES_CONS_INFLUX:
                InfluxResConsumptionBenchmark.run();
                break;
            default:
                logger.error(Constants.LOG_MARKER, "Invalid app mode");
        }
    }

    private static AppMode menuInteraction(String clArg) {
        switch(clArg) {
            case "p":
                logger.info(Constants.LOG_MARKER, "Started application in PORTING mode");

                PortingMenuItem source = getValidPortingMenuItemInput("Select data source. Press:");
                PortingMenuItem destination = getValidPortingMenuItemInput("Select data destination. Press:");

                if(source == REMOTE_MONGODB && destination == LOCAL_INFLUXDB) {
                    MongoClient mongoClient = MongoDbConnection.connect(Constants.MONGO_REMOTE_CON_STR);
                    mongoSource = mongoClient.getDatabase(Constants.MONGO_DB_REMOTE);
                    return PORT_MONGO_INFLUX;
                }
                else if(source == LOCAL_MONGODB && destination == LOCAL_INFLUXDB) {
                    MongoClient mongoClient = MongoDbConnection.connect(Constants.MONGO_LOCAL_CON_STR);
                    mongoSource = mongoClient.getDatabase(Constants.MONGO_DB_LOCAL);
                    return PORT_MONGO_INFLUX;
                }
                else if(source == REMOTE_MONGODB && destination == LOCAL_MONGODB) {
                    MongoClient mongoSourceClient = MongoDbConnection.connect(Constants.MONGO_REMOTE_CON_STR);
                    mongoSource = mongoSourceClient.getDatabase(Constants.MONGO_DB_REMOTE);
                    MongoClient mongoDestinationClient = MongoDbConnection.connect(Constants.MONGO_LOCAL_CON_STR);
                    mongoDestination = mongoDestinationClient.getDatabase(Constants.MONGO_DB_LOCAL);
                    return PORT_MONGO_MONGO;
                }
                else
                    return AppMode.INVALID;

            case "b":
                logger.info(Constants.LOG_MARKER, "Started application in BENCHMARKING mode");

                BenchmarkMenuItem benchmarkType = getValidBenchmarkMenuItemInput("Select benchmark type. Press:");
                switch(benchmarkType) {
                    case INFORMATION_RETRIEVAL:
                        return BENCHMARK_INF_RET;
                    case MONGO_RESOURCE_CONSUMPTION:
                        return BENCHMARK_RES_CONS_MONGO;
                    case INFLUX_RESOURCE_CONSUMPTION:
                        return BENCHMARK_RES_CONS_INFLUX;
                    default:
                        return AppMode.INVALID;
                }

            default:
                logger.error(Constants.LOG_MARKER, "Invalid CL arguments");
                return AppMode.INVALID;
        }
    }

    private static void portingFromMongoToInflux() {
        String targetCollection = getValidPortingStringInput("Specify exact name of source collection:");
        String targetBucket = getValidPortingStringInput("Specify exact name of destination bucket:");

        List<Document> mongoDocs = MongoDbRead.readAsDocument(mongoSource, targetCollection);
        logger.debug(Constants.LOG_MARKER, "Number of BSON documents = " + mongoDocs.size());

        List<SensorData> influxSeries = transformFromMongoToInflux(mongoDocs);
        logger.debug(Constants.LOG_MARKER, "Number of InfluxDB points = " + influxSeries.size());

        InfluxDBClient influxDestination = InfluxDbConnection.connect(Constants.HOST, Constants.TOKEN, targetBucket, Constants.ORG);
        InfluxDbWrite.write(influxDestination, influxSeries);
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

}
