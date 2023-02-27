package tekion.assignment2.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tekion.assignment2.Connection.ConnectMongo;
import tekion.assignment2.Repository.PlayerRepository;
import tekion.assignment2.Repository.TeamRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentaryRepository {
    MongoDatabase database = ConnectMongo.getConnection() ;

  TeamRepository teamRepository=new TeamRepository();

  PlayerRepository playerRepository=new PlayerRepository();
   MongoCollection<Document> commentaryCollection = database.getCollection("commentary") ;
   public void addMatchId(int matchId,int team1,int team2)
   {
       Document commentary = new Document("_id", new ObjectId()) ;
       commentary.append("matchId",matchId);
       commentaryCollection.insertOne(commentary);
   }
   public void innings1(Document doc,int matchId)
   {
       commentaryCollection.updateOne(Filters.eq("matchId", matchId), Updates.set("Innings1",doc));
   }
    public void innings2(Document doc,int matchId)
    {
        commentaryCollection.updateOne(Filters.eq("matchId", matchId), Updates.set("Innings2",doc));
    }
    public Document addballcommentary(int strike1, int strike2, int bowler, int ballResult,int ballNumber,Document commentary) {
       Document ballcommentary= new Document("_id", new ObjectId()) ;
        ballcommentary.append("bowlNumber",ballNumber)
                .append("bowlerId",bowler)
                .append("bowlerName",playerRepository.getPlayerName(bowler))
                .append("strike1Id",strike1)
                .append("strike1Name",playerRepository.getPlayerName(strike1))
                .append("strike2Id",strike2)
                .append("strike2Name",playerRepository.getPlayerName(strike2));
        if(ballResult==7)
        {
            String s="Wicket";
            ballcommentary.append("Result",s);
        }
        else
        {
            String s=ballResult+"Runs";
            ballcommentary.append("Result",s);
        }
        String s="ball"+ballNumber;
   commentary.append(s,ballcommentary);
   return commentary;
    }

    public Document getMatchCommentary(int matchId) {
        List<Document> commentarys = commentaryCollection.find(Filters.eq("matchId",matchId)).limit(1).into(new ArrayList<>());
        return (Document) commentarys.get(0);
    }
}
