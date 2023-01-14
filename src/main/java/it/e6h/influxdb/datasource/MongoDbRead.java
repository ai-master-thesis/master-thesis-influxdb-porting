package it.e6h.influxdb.datasource;

import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.e6h.influxdb.Constants;
import it.e6h.influxdb.datasource.model.Latest;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDbRead {

    public static List<Document> readAsDocument(MongoClient mongoClient) {
        try  {
            MongoDatabase smcTelemetryDB = mongoClient.getDatabase("smactory-telemetry");

            //XXX Mock
//            MongoCollection<Document> latest52Collection = smcTelemetryDB.getCollection("latest_52_1231_289");
//            latest52Collection.find().into(docs);

            List<MongoCollection<Document>> targetCollections = getTargetCollections(smcTelemetryDB);

            List<Document> docs = getDocuments(targetCollections);

            return docs;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<MongoCollection<Document>> getTargetCollections(MongoDatabase db) {
        ListCollectionsIterable<Document> allCollections = db.listCollections();

        List<MongoCollection<Document>> targetCollections = new ArrayList<>();
        for(Document col: allCollections) {
            String colName = col.getString("name");
            if(colName.contains("_" + Constants.TARGET_GROUP + "_")) {
                targetCollections.add(db.getCollection(colName));
            }

            //XXX
//            if(targetCollections.size() >= 2)
//                break;
        }
        return targetCollections;
    }

    private static List<Document> getDocuments(List<MongoCollection<Document>> collections) {
        List<Document> docs = new ArrayList<>();
        List<Document> result = new ArrayList<>();

        for(MongoCollection<Document> col: collections) {
            col.find().into(docs);
            result.addAll(docs);
        }

        return result;
    }

    public static List<Latest> readAsPojo(MongoClient mongoClient) {
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
