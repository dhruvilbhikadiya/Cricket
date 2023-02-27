package tekion.assignment2.Repository;

import org.springframework.stereotype.Repository;
import tekion.assignment2.Connection.ConnectMongo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
@Repository
public class PlayerRepository
{
    MongoDatabase database = ConnectMongo.getConnection() ;
    MongoCollection<Document> playerCollection = database.getCollection("players") ;
    public String getPlayerName(int playerId)
    {
        List<Document> players = playerCollection.find(Filters.eq("playerId",playerId)).limit(1).into(new ArrayList<>());
        return (String) players.get(0).get("playerName") ;
    }

    public void increaseBowlerThrownBalls(int playerId)
    {

        playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("bowlerTotalThrownBalls",1)) ;
    }

    public void increasePlayerPlayedBalls(int playerId)
    {

        playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("playerTotalPlayedBalls",1)) ;
    }

    public void increasePlayerTotalOut(int playerId)
    {
        playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("playerTotalOut",1)) ;
    }

    public void increaseBowlerTakenWickets(int playerId)
    {

        playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("bowlerTotalTakenWickets",1)) ;
    }

    public void increaseBowlerGivenRuns(int playerId)
    {
        playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("bowlerTotalGivenRuns",1)) ;
    }

    public void increasePlayerScore(int playerId, int runs)
    {
        playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("playerTotalScore",runs)) ;
        if(runs==4)
        {

            playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("playerTotal4s",1)) ;
        }
        else if(runs==6)
        {
            playerCollection.updateOne(Filters.eq("playerId", playerId), Updates.inc("playerTotal6s",1)) ;
        }
    }

    public int getPlayerScore(int playerId)
    {
        List<Document> players = playerCollection.find(Filters.eq("playerId",playerId)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("playerTotalScore") ;
    }

    public int getPlayerPlayedBalls(int playerId)
    {
        List<Document> players = playerCollection.find(Filters.eq("playerId",playerId)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("playerTotalPlayedBalls") ;
    }

    public int getPlayer4s(int playerId)
    {
        List<Document> players = playerCollection.find(Filters.eq("playerId",playerId)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("playerTotal4s") ;
    }

    public int getPlayer6s(int playerId)
    {
        List<Document> players = playerCollection.find(Filters.eq("playerId",playerId)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("playerTotal6s") ;
    }

    public int getBowlerGivenRuns(int playerId)
    {
        List<Document> players = playerCollection.find(Filters.eq("playerId",playerId)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("bowlerTotalGivenRuns") ;
    }

    public int getBowlerThrownBalls(int playerId)
    {
        List<Document> players = playerCollection.find(Filters.eq("playerId",playerId)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("bowlerTotalThrownBalls") ;
    }

    public int getBowlerTakenWickets(int playerId)
    {
        List<Document> players = playerCollection.find(Filters.eq("playerId",playerId)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("bowlerTotalTakenWickets") ;
    }

    public Document getPlayer(int playerId) {
        List<Document> players = playerCollection.find(Filters.eq("playerId",playerId)).limit(1).into(new ArrayList<>());
         Document player=(Document) players.get(0) ;
         return player;
    }
}
