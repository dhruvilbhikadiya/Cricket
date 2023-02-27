package tekion.assignment2.Connection;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectMongo
{
    public static MongoDatabase getConnection()
    {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        MongoClient mongoClient = new MongoClient("localhost", 27017) ;
        return mongoClient.getDatabase("learn");
    }

    public static void createIndexes(String s1, String s2, String s3, String s4)
    {
        IndexOptions indexOptions = new IndexOptions().unique(true);
        MongoDatabase mongoDatabase = ConnectMongo.getConnection() ;
        MongoCollection<Document> collection1 = mongoDatabase.getCollection(s1) ;
        MongoCollection<Document> collection2 = mongoDatabase.getCollection(s3) ;
        collection1.createIndex(Indexes.descending(s2), indexOptions) ;
        collection2.createIndex(Indexes.descending(s4), indexOptions) ;
        //creating batsman and bowler indexes
        MongoCollection<Document> batsmanCollection = mongoDatabase.getCollection("batsman") ;
        MongoCollection<Document> bowlerCollection = mongoDatabase.getCollection("bowler") ;
        MongoCollection<Document> scoreboardCollection = mongoDatabase.getCollection("scoreboards") ;
       scoreboardCollection.createIndex(Indexes.descending("matchId"), indexOptions) ;
        MongoCollection<Document> commentaryCollection = mongoDatabase.getCollection("commentary") ;
        commentaryCollection.createIndex(Indexes.descending("matchId"), indexOptions) ;
        batsmanCollection.createIndex(Indexes.compoundIndex(Indexes.descending("batsmanId"),Indexes.descending("batsmanMatchId")),indexOptions);
        bowlerCollection.createIndex(Indexes.compoundIndex(Indexes.descending("bowlerId"),Indexes.descending("bowlerMatchId")),indexOptions);

    }
}
