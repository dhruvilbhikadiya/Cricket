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
public class TeamRepository
{
    MongoDatabase database = ConnectMongo.getConnection() ;
    MongoCollection<Document> teamCollection = database.getCollection("teams") ;
    MongoCollection<Document> playerCollection = database.getCollection("players") ;
    public TeamRepository()
    {
        System.out.println("Team Repository created");
    }
    public void clearData(int teamId)
    {

        teamCollection.updateOne(Filters.eq("teamId",teamId), Updates.combine(
                Updates.set("teamRunsInMatch",0),
                Updates.set("teamWicketsInMatch",0),
                Updates.set("teamBallsInMatch",0)
        )) ;

        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        ArrayList<Integer> playerIds = (ArrayList<Integer>)players.get(0).get("players");
        int cnt = 0 ;
        for(Integer i:playerIds)
        {
            playerCollection.updateOne(Filters.eq("playerId",i),
                    Updates.combine(
                            Updates.set("playerScore",0),
                            Updates.set("player4s",0),
                            Updates.set("player6s",0),
                            Updates.set("playerPlayedBalls",0)
                    )) ;
            if(cnt>7)
            {
                playerCollection.updateOne(Filters.eq("playerId",i),
                        Updates.combine(
                                Updates.set("bowlerTakenWickets",0),
                                Updates.set("bowlerThrownBalls",0),
                                Updates.set("bowlerGivenRuns",0)
                        )) ;
            }
            cnt++ ;

        }
    }

    public String getTeamName(int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        return (String) players.get(0).get("teamName") ;

    }
    public Document getTeam(int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        Document team=(Document)players.get(0) ;
        return team;
    }

    public int getTeamBallsInMatch(int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("teamBallsInMatch") ;
    }

    public int getTeamRunsInMatch(int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("teamRunsInMatch") ;
    }

    public int getTeamWicketsInMatch(int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        return (Integer) players.get(0).get("teamWicketsInMatch") ;
    }

    public void increaseTeamBallsInMatch(int teamId)
    {
        teamCollection.updateOne(Filters.eq("teamId", teamId), Updates.inc("teamBallsInMatch",1)) ;
    }

    public void increaseTeamRunsInMatch(int teamId, int runs)
    {
        teamCollection.updateOne(Filters.eq("teamId", teamId), Updates.inc("teamRunsInMatch",runs)) ;
    }

    public int getPlayer(int playerNum, int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        ArrayList<Integer> playerIds = (ArrayList<Integer>)players.get(0).get("players");
        return playerIds.get(playerNum) ;
    }

    public void increaseTeamWicketsInMatch(int teamId)
    {
        teamCollection.updateOne(Filters.eq("teamId", teamId), Updates.inc("teamWicketsInMatch",1)) ;
    }

    public ArrayList<Integer> getPlayerList(int teamId)
    {
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        return (ArrayList<Integer>)players.get(0).get("players");
    }

}
