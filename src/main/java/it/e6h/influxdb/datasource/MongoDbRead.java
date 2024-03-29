package it.e6h.influxdb.datasource;

import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.e6h.influxdb.Constants;
import it.e6h.influxdb.datasource.model.Latest;
import it.e6h.influxdb.model.ValueType;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public class MongoDbRead {

    public static List<Document> readAsDocument(MongoDatabase db, String targetCollectionName) {
        try  {
            MongoCollection<Document> targetCollection = db.getCollection(targetCollectionName);

            List<Document> docs = getAllDocuments(targetCollection);

            return docs;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static List<Document> getAllDocuments(MongoCollection<Document> collection) {
        List<Document> docs = new ArrayList<>();

        collection.find().into(docs);

        return docs;
    }

    public static List<Document> getTopByItemIdAndProperty(
            MongoCollection<Document> collection,
            ValueType type, Long itemId, String property) {
        List<Document> docs = new ArrayList<>();

        Bson filter;
        if(type != null)
            filter = and(
                    eq(Constants.BSON_TYPE_KEY, type),
                    eq(Constants.BSON_ITEM_ID_KEY, itemId),
                    eq(Constants.BSON_PROPERTY_KEY, property));
        else
            filter = and(
                    eq(Constants.BSON_ITEM_ID_KEY, itemId),
                    eq(Constants.BSON_PROPERTY_KEY, property));

        collection.find(filter)
                .sort(descending(Constants.BSON_TIMESTAMP_KEY))
                .limit(Constants.TARGET_RECORDS_N)
                .into(docs);

        return docs;
    }

    //XXX Not used
    private static MongoCollection<Document> getTargetCollection(MongoDatabase smcTelemetryDB) {
        //XXX
//        String name = String.format("sensor_data_%s", Constants.TARGET_GROUP);
        String name = String.format("latest_%s_%s_%s", Constants.TARGET_GROUP, Constants.TARGET_ITEM_ID, Constants.TARGET_PROPERTY_ID);
        return smcTelemetryDB.getCollection(name);
    }

    //XXX Not used
    private static List<MongoCollection<Document>> getLatestCollections(MongoDatabase db) {
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

    //XXX Not used
    private static List<Document> getDocumentsFromCollections(List<MongoCollection<Document>> collections) {
        List<Document> docs = new ArrayList<>();
        List<Document> result = new ArrayList<>();

        for(MongoCollection<Document> col: collections) {
            col.find().into(docs);
            result.addAll(docs);
        }

        return result;
    }

    //XXX Not used
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
