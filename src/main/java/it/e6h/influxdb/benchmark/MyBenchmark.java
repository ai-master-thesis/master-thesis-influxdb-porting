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

public class MyBenchmark {
    private static Logger logger = LoggerFactory.getLogger(MyBenchmark.class);

    public static void run() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MyBenchmark.class.getSimpleName())
                .forks(Constants.BenchmarkParams.FORKS)
                .threads(Constants.BenchmarkParams.THREADS)
                .mode(Constants.BenchmarkParams.MODE)
                .warmupIterations(Constants.BenchmarkParams.WARMUP_ITERATIONS)
                .warmupBatchSize(Constants.BenchmarkParams.WARMUP_BATCH_SIZE)
                .measurementIterations(Constants.BenchmarkParams.MEASUREMENT_ITERATIONS)
                .measurementBatchSize(Constants.BenchmarkParams.MEASUREMENT_BATCH_SIZE)
                .timeUnit(Constants.BenchmarkParams.TIME_UNIT)
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Benchmark)
    public static class MongoDbConf {
        public MongoClient mongoClient = MongoDbConnection.connect(System.getProperty("mongodb.local.uri"));
        public String db = Constants.MONGO_DB_LOCAL;
    }

    @State(Scope.Benchmark)
    public static class InfluxDbConf {
        public InfluxDBClient influxClient = InfluxDbConnection.connect(Constants.HOST, Constants.TOKEN, Constants.BUCKET_FILTER, Constants.ORG);
    }

    @Benchmark
    public static List<Document> readLatestFromMongoDb(MongoDbConf mongoDbConf) {
        try  {
            MongoDatabase smcTelemetryDB = mongoDbConf.mongoClient.getDatabase(mongoDbConf.db);

            MongoCollection<Document> targetCollection = MongoDbRead.getLatestTargetCollection(smcTelemetryDB);

            List<Document> docs = MongoDbRead.getAllDocuments(targetCollection);
            logger.debug(Constants.LOG_MARKER, "Number of read BSON documents = " + docs.size());

            return docs;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public static List<FluxRecord> readLatestFromInfluxDb(InfluxDbConf influxDbConf) {
        try  {
            List<FluxRecord> records = InfluxDbRead.getLatest(influxDbConf.influxClient);
            logger.debug(Constants.LOG_MARKER, "Number of read Flux records = " + records.size());

            return records;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
