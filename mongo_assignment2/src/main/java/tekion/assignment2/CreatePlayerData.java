package tekion.assignment2;

import org.springframework.stereotype.Service;
import tekion.assignment2.Connection.ConnectMongo;
import tekion.assignment2.Repository.PlayerRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
@Service
public class CreatePlayerData {
    PlayerRepository playerRepository=new PlayerRepository();
    public void createPlayers(int matchId,int teamId)
    {
        MongoDatabase database = ConnectMongo.getConnection() ;
        MongoCollection<Document> teamCollection = database.getCollection("teams") ;
        MongoCollection<Document> playerCollection = database.getCollection("players") ;
        MongoCollection<Document> batsmanCollection = database.getCollection("batsman") ;
        MongoCollection<Document> bowlerCollection = database.getCollection("bowler") ;
        List<Document> players = teamCollection.find(Filters.eq("teamId",teamId)).limit(1).into(new ArrayList<>());
        ArrayList<Integer> playerIds = (ArrayList<Integer>)players.get(0).get("players");
        for(int i=0;i<11;i++)
        {
            Document batsman = new Document("_id", new ObjectId()) ;
            String playerName=playerRepository.getPlayerName(playerIds.get(i));
            batsman.append("batsmanId",playerIds.get(i))
                    .append("batsmanName",playerName)
                    .append("batsmanMatchId",matchId)
                    .append("batsmanScore",0)
                    .append("batsmanPlayedBalls",0)
                    .append("batsman4s",0)
                    .append("batsman6s",0);
            if(i>=7)
            {Document bowler = new Document("_id", new ObjectId()) ;
                bowler.append("bowlerId",playerIds.get(i))
                        .append("bowlerName",playerName)
                        .append("bowlerMatchId",matchId)
                        .append("bowlerTakenWickets",0)
                        .append("bowlerThrownBalls",0)
                        .append("bowlerGivenRuns",0);
                bowlerCollection.insertOne(bowler);
            }
            batsmanCollection.insertOne(batsman);
        }
    }
}
