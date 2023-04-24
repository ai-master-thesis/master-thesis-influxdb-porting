package it.e6h.influxdb.benchmark;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
import it.e6h.influxdb.util.Util;
import org.bson.Document;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InfRetrievalRandBenchmark {
    private static Logger logger = LoggerFactory.getLogger(InfRetrievalRandBenchmark.class);
    private static Long exeCounter = 0L;
    private static Long exeCounterTotal = 0L;

    public static void run() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(InfRetrievalRandBenchmark.class.getSimpleName())
                .forks(Constants.InfRetrievalBenchmarkParams.FORKS) // 0 for debugging
                .threads(Constants.InfRetrievalBenchmarkParams.THREADS)
                .mode(Constants.InfRetrievalBenchmarkParams.MODE)
                .warmupIterations(Constants.InfRetrievalBenchmarkParams.WARMUP_ITERATIONS)
                .warmupTime(Constants.InfRetrievalBenchmarkParams.WARMUP_TIME)
                .measurementIterations(Constants.InfRetrievalBenchmarkParams.MEASUREMENT_ITERATIONS)
                .measurementTime(Constants.InfRetrievalBenchmarkParams.MEASUREMENT_TIME)
                .timeUnit(Constants.InfRetrievalBenchmarkParams.TIME_UNIT)
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Benchmark)
    public static class InfRetrievalBenchmarkState {
        String mongoConnectionString = Constants.MONGO_LOCAL_CON_STR;
        String mongoDbName = Constants.MONGO_DB_LOCAL;
        String sensorDataDatasetName = String.format("sensor_data_%s", Constants.TARGET_GROUP);
        List<ItemProperties> itemPropertiesList;
        ItemProperty itemProperty;

        @Setup(Level.Trial)
        public void atBenchmarkStart() {
            final JsonMapper mapper = new JsonMapper();
            final File input = new File(
                    InfluxDbRead.class.getClassLoader().getResource("sensor_data_52-propByItem.jsonl").getFile()
            );

            itemPropertiesList = new ArrayList<>();
            try (MappingIterator<ItemProperties> it = mapper.readerFor(ItemProperties.class)
                    .readValues(input)) {
                while (it.hasNextValue()) {
                    ItemProperties v = it.nextValue();
                    itemPropertiesList.add(v);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Setup(Level.Invocation)
        public void atInvocationStart() {
            itemProperty = Util.getRandomItemProperty(itemPropertiesList);
        }

        @TearDown(Level.Invocation)
        public void atInvocationEnd() {
            exeCounter++;
        }

        @TearDown(Level.Iteration)
        public void atIterationEnd() {
            logger.info(Constants.LOG_MARKER, String.format("Number of executions in the iteration: %d", exeCounter));
            exeCounterTotal += exeCounter;
            exeCounter = 0L;
            logger.info(Constants.LOG_MARKER, "Execution counter reset");
        }

        @TearDown(Level.Trial)
        public void atBenchmarkEnd() {
            logger.info(Constants.LOG_MARKER, String.format("Number of executions in total: %d", exeCounterTotal));
            exeCounterTotal = 0L;
            logger.info(Constants.LOG_MARKER, "Execution counter reset");
        }
    }

    @Benchmark
    public static List<Document> mongoReadFromSensorDataRand(InfRetrievalBenchmarkState state) {
        try  {
            MongoClient client = MongoDbConnection.connect(state.mongoConnectionString);
            MongoDatabase db = client.getDatabase(state.mongoDbName);
            MongoCollection<Document> collection = db.getCollection(state.sensorDataDatasetName);

            List<Document> docs = MongoDbRead.getTopByItemIdAndProperty(
                    collection,
                    null,
                    state.itemProperty.getItemId(),
                    state.itemProperty.getProperty());
//            logger.debug(Constants.LOG_MARKER, "Number of read BSON documents = " + docs.size());

            return docs;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public static List<FluxRecord> influxReadFromSensorDataRand(InfRetrievalBenchmarkState state) {
        try  {
            InfluxDBClient client = InfluxDbConnection.connect(
                    Constants.HOST, Constants.TOKEN, state.sensorDataDatasetName, Constants.ORG);

            List<FluxRecord> records = InfluxDbRead.getTopByItemIdAndPropertyRC(
                    client, state.sensorDataDatasetName,
                    state.itemProperty.getItemId(), state.itemProperty.getProperty()
            );

            return records;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
