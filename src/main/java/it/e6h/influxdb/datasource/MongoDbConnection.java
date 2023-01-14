package it.e6h.influxdb.datasource;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import it.e6h.influxdb.Constants;
import it.e6h.influxdb.Main;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDbConnection {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static MongoClient connect() {
        ConnectionString connectionString = new ConnectionString(System.getProperty("mongodb.uri"));

//        MongoClientSettings clientSettings = confPojo(connectionString);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        MongoClient mongoClient = MongoClients.create(clientSettings);

        List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
        databases.forEach(db -> logger.debug(db.toJson()));

        return mongoClient;
    }

    private static MongoClientSettings confPojo(ConnectionString connectionString) {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        return settings;
    }

}
