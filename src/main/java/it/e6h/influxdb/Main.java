package it.e6h.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.mongodb.client.MongoClient;
import it.e6h.influxdb.datasource.MongoDbConnection;
import it.e6h.influxdb.datasource.MongoDbRead;

public class Main {

    public static void main(final String[] args) {
        MongoClient mongoClient = MongoDbConnection.connect();
        MongoDbRead.read(mongoClient);

        InfluxDBClient influxClient = InfluxDbConnection.connect(Constants.HOST, Constants.TOKEN, Constants.BUCKET, Constants.ORG);
        InfluxDbWrite.write(influxClient);
    }

}
