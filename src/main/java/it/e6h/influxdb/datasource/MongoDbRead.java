package it.e6h.influxdb.datasource;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.e6h.influxdb.datasource.model.Latest;

import java.util.ArrayList;
import java.util.List;

public class MongoDbRead {

    public static List<Latest> read(MongoClient mongoClient) {
        try  {
            MongoDatabase smcTelemetryDB = mongoClient.getDatabase("smactory-telemetry");

            MongoCollection<Latest> latest52Collection = smcTelemetryDB.getCollection("latest_52_1231_289", Latest.class);

            List<Latest> docs = new ArrayList<>();
            latest52Collection.find().into(docs);

            return docs;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
