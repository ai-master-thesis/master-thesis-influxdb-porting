package it.e6h.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.mongodb.client.MongoClient;
import it.e6h.influxdb.datasource.MongoDbConnection;
import it.e6h.influxdb.datasource.MongoDbRead;
import it.e6h.influxdb.model.Latest;
import it.e6h.influxdb.model.Mapper;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(final String[] args) {
        MongoClient mongoClient = MongoDbConnection.connect();
        List<Document> mongoDocs = MongoDbRead.readAsDocument(mongoClient);

        List<Latest> influxSeries = new ArrayList<>();

        for(Document doc: mongoDocs) {
            System.out.println("BSON = " + doc);

            influxSeries.add(Mapper.documentToInfluxPojo(doc));
        }

        for(it.e6h.influxdb.model.Latest point: influxSeries) {
            System.out.println("it.e6h.influxdb.model.Latest point = " + point);
        }

        InfluxDBClient influxClient = InfluxDbConnection.connect(Constants.HOST, Constants.TOKEN, Constants.BUCKET, Constants.ORG);
        InfluxDbWrite.write(influxClient, influxSeries);
    }

}
