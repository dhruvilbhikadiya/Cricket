package tekion.assignment2;

import org.springframework.stereotype.Service;
import tekion.assignment2.Connection.ConnectMongo;
import tekion.assignment2.Repository.TeamRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.gte;

@Service
public class CreateTeam
{
    public CreateTeam(){}
    public void createTeam(String URL) throws FileNotFoundException {
        File file = new File(URL) ;
        Scanner sc = new Scanner(file) ;
        while (sc.hasNextLine())
        {
            addTeam(sc.nextLine());
        }
    }
    public void addTeam(String players)
    {
        String[] s = players.split(",") ;
        saveTeam(s) ;
    }

    public int saveTeam(String[] s)
    {
        MongoDatabase database = ConnectMongo.getConnection() ;
        MongoCollection<Document> teamcollection = database.getCollection("teams") ;
        MongoCollection<Document> playercollection = database.getCollection("players") ;
        ConnectMongo.createIndexes("teams","teamId","players","playerId");

        List<Document> players = playercollection.find(gte("playerId",0)).limit(1).into(new ArrayList<>());
        int playerId = (players.size()==0) ? 0 : (int)players.get(0).get("playerId")+1 ;

        List<Document> team = teamcollection.find(gte("teamId",0)).limit(1).into(new ArrayList<>());
        int teamId = (team.size()==0) ? 0 : (int)team.get(0).get("teamId")+1 ;
       // System.out.println(playerId);
        List<Integer> playerIds = new ArrayList<>() ;

        for(int i=1;i<=11;i++)
        {
            String type = (i>7) ? "Batsman" : "Bowler" ;
            Document player = new Document("_id", new ObjectId()) ;

            player.append("playerId",playerId)
                    .append("playerName",s[i])
                    .append("playerType",type)
                    .append("playerTeam",s[0])
                    .append("playerTotalScore",0)
                    .append("playerTotal4s",0)
                    .append("playerTotal6s",0)
                    .append("playerTotalPlayedBalls",0)
                    .append("playerTotalOut",0) ;

            if(i>7)
            {
                player.append("bowlerTotalTakenWickets",0)
                        .append("bowlerTotalThrownBalls",0)
                        .append("bowlerTotalGivenRuns",0);

            }

            playercollection.insertOne(player);
            playerIds.add(playerId++) ;
        }
        teamcollection.insertOne(new Document()
                .append("teamId",teamId)
                .append("teamName", s[0])
                .append("teamRunsInMatch",0)
                .append("teamWicketsInMatch",0)
                .append("teamBallsInMatch",0)
                .append("players",playerIds)
        ) ;
        return 1 ;
    }
}
