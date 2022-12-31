package it.e6h.influxdb.datasource;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.e6h.influxdb.datasource.model.Latest;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDbRead {

    public static void read(MongoClient mongoClient) {
        try  {
            MongoDatabase smcTelemetryDB = mongoClient.getDatabase("smactory-telemetry");

            MongoCollection<Document> latest52Collection = smcTelemetryDB.getCollection("latest_52_1231_289");

            List<Document> docs = new ArrayList<>();
            latest52Collection.find().into(docs);
            System.out.println("docs: " + docs);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
