package it.e6h.influxdb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import org.bson.BsonValue;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class MongoDbWrite {
    private static Logger logger = LoggerFactory.getLogger(MongoDbWrite.class);


    public static void write(MongoClient mongoClient, List<Document> mongoDocs) {
        try {
            MongoDatabase database = mongoClient.getDatabase(Constants.MONGO_DB_LOCAL);

            MongoCollection<Document> targetCollection = getTargetCollection(database);

            InsertManyResult result = targetCollection.insertMany(mongoDocs);
            Map<Integer, BsonValue> insIds = result.getInsertedIds();
            logger.info(Constants.LOG_MARKER, String.format("Inserted %s documents", insIds.size()));
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static MongoCollection<Document> getTargetCollection(MongoDatabase database) {
        //XXX
//        String name = String.format("sensor_data_%s", Constants.TARGET_GROUP);
        String name = String.format("latest_%s_%s_%s", Constants.TARGET_GROUP, Constants.TARGET_ITEM_ID, Constants.TARGET_PROPERTY_ID);
        return database.getCollection(name);
    }

}
