package tekion.assignment2.Repository;

import org.springframework.stereotype.Repository;
import tekion.assignment2.Connection.ConnectMongo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
@Repository
public class BatsmanRepository {
    MongoDatabase database = ConnectMongo.getConnection() ;
    MongoCollection<Document> batsmanCollection = database.getCollection("batsman") ;
    public void increasePlayerPlayedBalls(int playerId, int matchId) {
        Bson Filter1 = Filters.eq("batsmanId",playerId);
        Bson Filter2 = Filters.eq("batsmanMatchId",matchId );
        batsmanCollection.updateOne(Filters.and(Filter1,Filter2 ), Updates.inc("batsmanPlayedBalls",1)) ;
    }

    public void increasePlayerScore(int playerId, int matchId, int runs) {
        Bson Filter1 = Filters.eq("batsmanId",playerId);
        Bson Filter2 = Filters.eq("batsmanMatchId",matchId );
        batsmanCollection.updateOne(Filters.and(Filter1,Filter2 ), Updates.inc("batsmanScore",runs)) ;
        if(runs==4)
        {
            //playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("player4s",1)) ;
            batsmanCollection.updateOne(Filters.and(Filter1,Filter2), Updates.inc("batsman4s",1)) ;
        }
        else if(runs==6)
        {
            //playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("player6s",1)) ;
           batsmanCollection.updateOne(Filters.and(Filter1,Filter2), Updates.inc("batsman6s",1)) ;
        }
    }
    public Document getPlayer(Integer playerId, int matchId) {
        Bson Filter1 = Filters.eq("batsmanId",playerId);
        Bson Filter2 = Filters.eq("batsmanMatchId",matchId );
        List<Document> players = batsmanCollection.find(Filters.and(Filter1,Filter2)).limit(1).into(new ArrayList<>());
        Document batsman=(Document)players.get(0);
        return batsman;
    }

    public int getPlayerScore(Integer playerId, int matchId) {
        Bson Filter1 = Filters.eq("batsmanId",playerId);
        Bson Filter2 = Filters.eq("batsmanMatchId",matchId );
        List<Document> players = batsmanCollection.find(Filters.and(Filter1,Filter2)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("batsmanScore") ;
    }
    public int getPlayerPlayedBalls(Integer playerId, int matchId) {
        Bson Filter1 = Filters.eq("batsmanId",playerId);
        Bson Filter2 = Filters.eq("batsmanMatchId",matchId );
        List<Document> players = batsmanCollection.find(Filters.and(Filter1,Filter2)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("batsmanPlayedBalls") ;
    }
    public int getPlayer4s(Integer playerId, int matchId) {
        Bson Filter1 = Filters.eq("batsmanId",playerId);
        Bson Filter2 = Filters.eq("batsmanMatchId",matchId );
        List<Document> players = batsmanCollection.find(Filters.and(Filter1,Filter2)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("batsman4s") ;
    }
    public int getPlayer6s(Integer playerId, int matchId) {
        Bson Filter1 = Filters.eq("batsmanId",playerId);
        Bson Filter2 = Filters.eq("batsmanMatchId",matchId );
        List<Document> players = batsmanCollection.find(Filters.and(Filter1,Filter2)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("batsman6s") ;
    }
}
