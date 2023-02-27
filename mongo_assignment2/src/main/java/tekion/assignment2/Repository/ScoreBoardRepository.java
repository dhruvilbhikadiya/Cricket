package tekion.assignment2.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tekion.assignment2.Connection.ConnectMongo;
import tekion.assignment2.Repository.BatsmanRepository;
import tekion.assignment2.Repository.BowlerRepository;
import tekion.assignment2.Repository.PlayerRepository;
import tekion.assignment2.Repository.TeamRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
@Repository
public class ScoreBoardRepository {
    MongoDatabase database = ConnectMongo.getConnection() ;

    TeamRepository teamRepository=new TeamRepository();

   PlayerRepository playerRepository=new PlayerRepository();

    BatsmanRepository batsmanRepository=new BatsmanRepository();

   BowlerRepository bowlerRepository=new BowlerRepository();

    MongoCollection<Document> scoreBoardCollection = database.getCollection("scoreboards");
    public void saveScoreBoard(int team1Id,int team2Id,String tossResultOutput,int matchId,String matchResult)
    {    Document scoreBoard = new Document("_id", new ObjectId()) ;
        scoreBoard.append("matchId",matchId)
                .append("team1Name",teamRepository.getTeamName(team1Id))
                .append("team2Name",teamRepository.getTeamName(team2Id))
                .append("tossResult",tossResultOutput);
        Document inning1=saveInnings(team1Id,team2Id,matchId);
        Document inning2=saveInnings(team2Id,team1Id,matchId);
        scoreBoard.append("innings1",inning1);
        scoreBoard.append("innings2",inning2);
        scoreBoard.append("MatchResult",matchResult);
        scoreBoardCollection.insertOne(scoreBoard);
    }
    private Document saveInnings(int team1Id,int team2Id,int matchId)
    {
        Document inning = new Document("_id", new ObjectId()) ;
        Document batting = new Document("_id", new ObjectId()) ;
        for(int i=0;i<11;i++)
        {   String s="Player"+i;
            Document batman=saveBatting(teamRepository.getPlayer(i,team1Id),matchId);
            batting.append(s,batman);
        }
        inning.append("Batting",batting);
        Document bowling = new Document("_id", new ObjectId()) ;
        for(int i=7;i<11;i++)
        {
            String s="Player"+i;
            Document bowler=saveBowling(teamRepository.getPlayer(i,team1Id),matchId);
            bowling.append(s,bowler);
        }
        inning.append("Bowling",bowling);
        Document inningResult = new Document("_id", new ObjectId()) ;
        inningResult.append("Score",teamRepository.getTeamRunsInMatch(team1Id))
                .append("Over", teamRepository.getTeamBallsInMatch(team1Id));
        inning.append("Result",inningResult);
        return inning;
    }
    private Document saveBowling(int playerId,int matchId)
    {
        Document player=new Document("_id",new ObjectId());
        player.append("playerName",playerRepository.getPlayerName(playerId))
                .append("playerThrownBalls",bowlerRepository.getBowlerThrownBalls(playerId,matchId))
                .append("playerGivenRuns",bowlerRepository.getBowlerGivenRuns(playerId,matchId))
                .append("playerTakenWickets",bowlerRepository.getBowlerTakenWickets(playerId,matchId));
        return player;
    }
    private Document saveBatting(int playerId,int matchId)
    {
        Document player=new Document("_id",new ObjectId());
        player.append("playerName",playerRepository.getPlayerName(playerId))
                .append("playerRubs",batsmanRepository.getPlayerScore(playerId,matchId))
                .append("playerBalls",batsmanRepository.getPlayerPlayedBalls(playerId,matchId))
                .append("player4s",batsmanRepository.getPlayer4s(playerId,matchId))
                .append("player6s",batsmanRepository.getPlayer6s(playerId,matchId));
        return player;
    }


    public int getScoreBoardColletionSize()
    {
        List<Document> players = scoreBoardCollection.find().into(new ArrayList<>());
        return players.size();
    }

    public Document getScoreBoard(int matchId) {
        List<Document> scoreboards = scoreBoardCollection.find(Filters.eq("matchId",matchId)).limit(1).into(new ArrayList<>());
        Document scoreboard=(Document)scoreboards.get(0);
        return scoreboard;
    }
}
