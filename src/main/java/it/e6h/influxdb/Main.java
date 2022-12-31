package it.e6h.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.mongodb.client.MongoClient;
import it.e6h.influxdb.datasource.MongoDbConnection;
import it.e6h.influxdb.datasource.MongoDbRead;
import it.e6h.influxdb.datasource.model.Latest;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(final String[] args) {
        MongoClient mongoClient = MongoDbConnection.connect();
        List<Latest> mongoDocs = MongoDbRead.read(mongoClient);

        List<it.e6h.influxdb.model.Latest> influxSeries = new ArrayList<>();

        for(Latest doc: mongoDocs) {
            System.out.println("it.e6h.influxdb.datasource.model.Latest doc = " + doc);

            influxSeries.add(doc.toInfluxPojo());
        }

        for(it.e6h.influxdb.model.Latest point: influxSeries) {
            System.out.println("it.e6h.influxdb.model.Latest point = " + point);
        }

        InfluxDBClient influxClient = InfluxDbConnection.connect(Constants.HOST, Constants.TOKEN, Constants.BUCKET, Constants.ORG);
        InfluxDbWrite.write(influxClient, influxSeries);
    }

}
