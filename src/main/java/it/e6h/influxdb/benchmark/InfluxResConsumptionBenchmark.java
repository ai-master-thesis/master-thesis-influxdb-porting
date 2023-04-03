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
import java.util.Random;

public class InfluxResConsumptionBenchmark {
    private static Logger logger = LoggerFactory.getLogger(InfluxResConsumptionBenchmark.class);
    private static Long exeCounter = 0L;

    public static void run() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(InfluxResConsumptionBenchmark.class.getSimpleName())
                .forks(Constants.ResConsumptionBenchmarkParams.FORKS) // 0 for debugging
                .threads(Constants.ResConsumptionBenchmarkParams.THREADS)
                .mode(Constants.ResConsumptionBenchmarkParams.MODE)
                .warmupIterations(Constants.ResConsumptionBenchmarkParams.WARMUP_ITERATIONS)
                .warmupTime(Constants.ResConsumptionBenchmarkParams.WARMUP_TIME)
                .measurementIterations(Constants.ResConsumptionBenchmarkParams.MEASUREMENT_ITERATIONS)
                .measurementTime(Constants.ResConsumptionBenchmarkParams.MEASUREMENT_TIME)
                .timeUnit(Constants.ResConsumptionBenchmarkParams.TIME_UNIT)
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Benchmark)
    public static class InfluxDbConf {
        String sensorDataBucketName;
        InfluxDBClient client;
        List<ItemProperties> itemPropertiesList;

        @Setup(Level.Trial)
        public void doSetup() {
            sensorDataBucketName = String.format("sensor_data_%s", Constants.TARGET_GROUP);
            client = InfluxDbConnection.connect(
                    Constants.HOST, Constants.TOKEN, sensorDataBucketName, Constants.ORG);

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

        @TearDown(Level.Iteration)
        public void doTearDown() {
            logger.info(Constants.LOG_MARKER, String.format("\nMethod executed %d times since start", exeCounter));
        }
    }

    @Benchmark
    public static List<FluxRecord> influxReadFromSensorData(InfluxDbConf influxDbConf) {
        try  {
            Random rand = new Random();
            int itemIdIndex = rand.nextInt(influxDbConf.itemPropertiesList.size());
            ItemProperties itemProperties = influxDbConf.itemPropertiesList.get(itemIdIndex);
            Long itemId = itemProperties.getItemId();
            List<String> properties = itemProperties.getProperties();
            int propertyIndex = rand.nextInt(properties.size());
            String property = properties.get(propertyIndex);

            List<FluxRecord> records = InfluxDbRead.getTopByItemIdAndPropertyRC(
                    influxDbConf.client, influxDbConf.sensorDataBucketName,
                    itemId, property
            );

            exeCounter++;
            return records;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
