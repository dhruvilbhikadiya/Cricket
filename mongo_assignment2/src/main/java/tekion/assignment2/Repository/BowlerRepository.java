package tekion.assignment2.Repository;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import tekion.assignment2.Connection.ConnectMongo;

import java.util.ArrayList;
import java.util.List;
@Repository
public class BowlerRepository {
    MongoDatabase database = ConnectMongo.getConnection() ;
    MongoCollection<Document> bowlerCollection = database.getCollection("bowler") ;
    public void increaseBowlerThrownBalls(int playerId, int matchId) {
        Bson Filter1 = Filters.eq("bowlerId",playerId);
        Bson Filter2 = Filters.eq("bowlerMatchId",matchId );
        bowlerCollection.updateOne(Filters.and(Filter1,Filter2 ), Updates.inc("bowlerThrownBalls",1)) ;
    }

    public void increaseBowlerTakenWickets(int playerId, int matchId) {
        Bson Filter1 = Filters.eq("bowlerId",playerId);
        Bson Filter2 = Filters.eq("bowlerMatchId",matchId );
        bowlerCollection.updateOne(Filters.and(Filter1,Filter2 ), Updates.inc("bowlerTakenWickets",1)) ;
    }

    public void increaseBowlerGivenRuns(int playerId, int matchId, int ballResult) {
        Bson Filter1 = Filters.eq("bowlerId",playerId);
        Bson Filter2 = Filters.eq("bowlerMatchId",matchId );
        bowlerCollection.updateOne(Filters.and(Filter1,Filter2 ), Updates.inc("bowlerGivenRuns",ballResult)) ;
    }

    public int getBowlerGivenRuns(Integer playerId, int matchId) {
        Bson Filter1 = Filters.eq("bowlerId",playerId);
        Bson Filter2 = Filters.eq("bowlerMatchId",matchId );
        List<Document> players = bowlerCollection.find(Filters.and(Filter1,Filter2)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("bowlerGivenRuns") ;
    }


    public int getBowlerThrownBalls(Integer playerId, int matchId) {
        Bson Filter1 = Filters.eq("bowlerId",playerId);
        Bson Filter2 = Filters.eq("bowlerMatchId",matchId );
        List<Document> players = bowlerCollection.find(Filters.and(Filter1,Filter2)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("bowlerThrownBalls") ;
    }

    public int getBowlerTakenWickets(Integer playerId, int matchId) {
        Bson Filter1 = Filters.eq("bowlerId",playerId);
        Bson Filter2 = Filters.eq("bowlerMatchId",matchId );
        List<Document> players = bowlerCollection.find(Filters.and(Filter1,Filter2)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("bowlerTakenWickets") ;
    }

    public Document getPlayer(int playerId, int matchId) {
        Bson Filter1 = Filters.eq("bowlerId",playerId);
        Bson Filter2 = Filters.eq("bowlerMatchId",matchId );
        List<Document> players = bowlerCollection.find(Filters.and(Filter1,Filter2)).limit(1).into(new ArrayList<>());
        Document bowler=null;
        if(players.size()!=0)
        bowler=(Document)players.get(0);
        return bowler;
    }
}
