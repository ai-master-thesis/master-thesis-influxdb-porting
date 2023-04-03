package it.e6h.influxdb.benchmark;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxRecord;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.e6h.influxdb.Constants;
import it.e6h.influxdb.InfluxDbConnection;
import it.e6h.influxdb.InfluxDbRead;
import it.e6h.influxdb.datasource.MongoDbConnection;
import it.e6h.influxdb.datasource.MongoDbRead;
import it.e6h.influxdb.model.ValueType;
import org.bson.Document;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InfRetrievalBenchmark {
    private static Logger logger = LoggerFactory.getLogger(InfRetrievalBenchmark.class);

    public static void run() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(InfRetrievalBenchmark.class.getSimpleName())
                .forks(Constants.InfRetrievalBenchmarkParams.FORKS)
                .threads(Constants.InfRetrievalBenchmarkParams.THREADS)
                .mode(Constants.InfRetrievalBenchmarkParams.MODE)
                .warmupIterations(Constants.InfRetrievalBenchmarkParams.WARMUP_ITERATIONS)
                .warmupBatchSize(Constants.InfRetrievalBenchmarkParams.WARMUP_BATCH_SIZE)
                .measurementIterations(Constants.InfRetrievalBenchmarkParams.MEASUREMENT_ITERATIONS)
                .measurementBatchSize(Constants.InfRetrievalBenchmarkParams.MEASUREMENT_BATCH_SIZE)
                .timeUnit(Constants.InfRetrievalBenchmarkParams.TIME_UNIT)
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Benchmark)
    public static class MongoDbConf {
        public String connectionString = System.getProperty("mongodb.local.uri");
        public String dbName = Constants.MONGO_DB_LOCAL;
        public String latestCollectionName = String.format("latest_%s_%s_%s",
                Constants.TARGET_GROUP, Constants.TARGET_ITEM_ID, Constants.TARGET_PROPERTY_ID);
        public String sensorDataCollectionName = String.format("sensor_data_%s", Constants.TARGET_GROUP);
    }

    @State(Scope.Benchmark)
    public static class InfluxDbConf {
        public String latestBucketName = String.format("latest_%s_%s_%s",
                Constants.TARGET_GROUP, Constants.TARGET_ITEM_ID, Constants.TARGET_PROPERTY_ID);
        public String sensorDataBucketName = String.format("sensor_data_%s", Constants.TARGET_GROUP);
    }

    @Benchmark
    public static List<Document> mongoReadFromLatest(MongoDbConf mongoDbConf) {
        try  {
            MongoClient client = MongoDbConnection.connect(mongoDbConf.connectionString);
            MongoDatabase db = client.getDatabase(mongoDbConf.dbName);
            MongoCollection<Document> collection = db.getCollection(mongoDbConf.latestCollectionName);

            List<Document> docs = MongoDbRead.getAllDocuments(collection);
            logger.debug(Constants.LOG_MARKER, "Number of read BSON documents = " + docs.size());

            return docs;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public static List<Document> mongoReadFromSensorData(MongoDbConf mongoDbConf) {
        try  {
            MongoClient client = MongoDbConnection.connect(mongoDbConf.connectionString);
            MongoDatabase db = client.getDatabase(mongoDbConf.dbName);
            MongoCollection<Document> collection = db.getCollection(mongoDbConf.sensorDataCollectionName);

            List<Document> docs = MongoDbRead.getTopByItemIdAndProperty(collection,
                    ValueType.NUMERIC,
                    Constants.TARGET_ITEM_ID,
                    Constants.TARGET_PROPERTY);
            logger.debug(Constants.LOG_MARKER, "Number of read BSON documents = " + docs.size());

            return docs;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public static List<FluxRecord> influxReadFromLatest(InfluxDbConf influxDbConf) {
        try  {
            InfluxDBClient client = InfluxDbConnection.connect(
                    Constants.HOST, Constants.TOKEN, influxDbConf.latestBucketName, Constants.ORG);

            List<FluxRecord> records = InfluxDbRead.getAll(client, influxDbConf.latestBucketName);
            logger.debug(Constants.LOG_MARKER, "Number of read Flux records = " + records.size());

            return records;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public static List<FluxRecord> influxReadFromSensorData(InfluxDbConf influxDbConf) {
        try  {
            InfluxDBClient client = InfluxDbConnection.connect(
                    Constants.HOST, Constants.TOKEN, influxDbConf.sensorDataBucketName, Constants.ORG);

            List<FluxRecord> records = InfluxDbRead.getTopByItemIdAndProperty(client, influxDbConf.sensorDataBucketName);
            logger.debug(Constants.LOG_MARKER, "Number of read Flux records = " + records.size());

            return records;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
