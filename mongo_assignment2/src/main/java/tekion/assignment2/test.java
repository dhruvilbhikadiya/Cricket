package tekion.assignment2;

import tekion.assignment2.Connection.ConnectMongo;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.slf4j.LoggerFactory;

public class test {
    public static void main(String[] args) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        ((Logger)rootLogger).setLevel(Level.OFF);
        MongoDatabase mongo= ConnectMongo.getConnection();
        MongoCollection<Document> comCollection=mongo.getCollection("commentary");
        Document doc=comCollection.find(Filters.eq("matchId",8)).first();
        System.out.println(doc);
        assert doc!=null;

        System.out.println("doc1");
        Document doc1=(Document) doc.get("Innings1");
        System.out.println(doc1);
        System.out.println("doc2");
        Document doc2=(Document) doc1.get("ball2");
        System.out.println(doc2);
    }
}
