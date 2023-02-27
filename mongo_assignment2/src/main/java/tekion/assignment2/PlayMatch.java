package tekion.assignment2;

import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tekion.assignment2.Connection.ConnectMongo;
import tekion.assignment2.Repository.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.gte;
@Service
public class PlayMatch {
    static int matchId = 0;
    private int oversOfMatch;

    TeamRepository teamRepository=new TeamRepository() ;

    PlayerRepository playerRepository=new PlayerRepository();

    BatsmanRepository batsmanRepository=new BatsmanRepository();

    BowlerRepository bowlerRepository=new BowlerRepository();

    ScoreBoardRepository scoreBoardRepository=new ScoreBoardRepository();

    CommentaryRepository commentaryRepository=new CommentaryRepository();
    private int team1Id, team2Id;
    Scanner sc = new Scanner(System.in);
    RandomNumberGenerator rg = new RandomNumberGenerator();
    String tossResult = "";
    String tossResultOutput="";
    String matchResult="";

    CreatePlayerData createPlayerData=new CreatePlayerData();
    public PlayMatch() {

    }
    public void playMatch(int team1Id,int team2Id)
    {
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        oversOfMatch = 60;
        MongoDatabase database= ConnectMongo.getConnection();
        MongoCollection<Document> scoreBoardCollection=database.getCollection("scoreboards");
        List<Document> matchSize=scoreBoardCollection.find(gte("matchId",0)).into(new ArrayList<>());
        System.out.println("matchSize.size()");
        System.out.println(matchSize.size());
        if(matchSize.size()==0)
            matchId=0;
        else
            matchId=(int)matchSize.get(0).get("matchId")+1;
        createMatchdata();
            toss();
           startMatch();
            printScoreBoard();
            teamRepository.clearData(team1Id) ;
            teamRepository.clearData(team2Id) ;
    }
    public void createMatchdata()
    {
            createPlayerData.createPlayers(matchId,team1Id);
            createPlayerData.createPlayers(matchId,team2Id);
    }
    public void toss() {
        System.out.print("\n" + teamRepository.getTeamName(team1Id) + " enter your toss guess (H/T) : ");
        char guessToss = sc.next().charAt(0);
        char generateToss = (rg.generateRandomNumber(1) == 0) ? 'H' : 'T';
        tossResult = (guessToss == generateToss) ? "0" : "1";
        String tossWinningTeam = (tossResult.equals("0")) ? teamRepository.getTeamName(team1Id) : teamRepository.getTeamName(team2Id);
        System.out.println(tossWinningTeam + " won the toss.");
        System.out.print("Choose Batting(0) or Bowling(1) : ");
        int answer = sc.nextInt();
        tossResult += answer;
    }
    public void startMatch() {
        System.out.print("\n");
        String[] s1 = new String[2];
        s1[0] = teamRepository.getTeamName(team1Id);
        s1[1] = teamRepository.getTeamName(team2Id);
        String[] s2 = new String[2];
        s2[0] = "Bat";
        s2[1] = "Bowl";
        tossResultOutput=s1[tossResult.charAt(0) - '0'] + " won the toss and choose to " +
                s2[tossResult.charAt(1) - '0'] + " first.";
        System.out.println(tossResultOutput+"\n");
        if (tossResult.equals("01") || tossResult.equals("10")) {
            int t = team1Id;
            team1Id = team2Id;
            team2Id = t;
        }
       commentaryRepository.addMatchId(matchId,team1Id,team2Id);
        Document inning1=startMatch(team1Id, team2Id, 1);
        commentaryRepository.innings1(inning1,matchId);
       Document inning2=startMatch(team2Id, team1Id, 2);
        commentaryRepository.innings2(inning1,matchId);
    }
    public Document startMatch(int team1Id, int team2Id, int flag) {
        Document ballcommentary= new Document("_id", new ObjectId()) ;
        System.out.println(teamRepository.getTeamName(team1Id) + " Batting : \n");
        int strike1 = 0;
        int strike2 = 1;
        int bowler = 7;
        int nextPlayer = 2;
        int nextBowler = 8;
        while (teamRepository.getTeamBallsInMatch(team1Id) < oversOfMatch) {
            if (flag == 2 && teamRepository.getTeamRunsInMatch(team2Id) < teamRepository.getTeamRunsInMatch(team1Id))
                break;
            teamRepository.increaseTeamBallsInMatch(team1Id);
            int ballResult = rg.generateRandomNumber(7);

            System.out.println(Over.convertToOvers(teamRepository.getTeamBallsInMatch(team1Id)) + " :- "
                    + playerRepository.getPlayerName(teamRepository.getPlayer(bowler,team2Id)) + " to "
                    + playerRepository.getPlayerName(teamRepository.getPlayer(strike1,team1Id)) + " , Result : " + ballResult + ".");
            //making changes
            batsmanRepository.increasePlayerPlayedBalls(teamRepository.getPlayer(strike1, team1Id),matchId);
            bowlerRepository.increaseBowlerThrownBalls(teamRepository.getPlayer(bowler, team2Id),matchId);
            playerRepository.increaseBowlerThrownBalls(teamRepository.getPlayer(bowler, team2Id));
            playerRepository.increasePlayerPlayedBalls(teamRepository.getPlayer(strike1, team1Id));
            ballcommentary=commentaryRepository.addballcommentary(strike1,strike2,bowler,ballResult,teamRepository.getTeamBallsInMatch(team1Id),ballcommentary);
            if (ballResult == 7) {
                teamRepository.increaseTeamWicketsInMatch(team1Id);
                playerRepository.increasePlayerTotalOut(teamRepository.getPlayer(strike1, team1Id));
                playerRepository.increaseBowlerTakenWickets(teamRepository.getPlayer(bowler, team2Id));
                bowlerRepository.increaseBowlerTakenWickets(teamRepository.getPlayer(bowler, team2Id),matchId);

                if (teamRepository.getTeamWicketsInMatch(team1Id) == 10)
                    break;
                strike1 = nextPlayer++;
            } else {
                teamRepository.increaseTeamRunsInMatch(team1Id, ballResult);
                playerRepository.increaseBowlerGivenRuns(teamRepository.getPlayer(bowler, team2Id));
                bowlerRepository.increaseBowlerGivenRuns(teamRepository.getPlayer(bowler, team2Id),matchId,ballResult);
                playerRepository.increasePlayerScore(teamRepository.getPlayer(strike1, team1Id),ballResult);
                batsmanRepository.increasePlayerScore(teamRepository.getPlayer(strike1, team1Id),matchId,ballResult);
                if (ballResult % 2 == 1) {
                    int temp = strike1;
                    strike1 = strike2;
                    strike2 = temp;
                }
            }
            if (teamRepository.getTeamBallsInMatch(team1Id) % 6 == 0) {
                int temp = strike1;
                strike1 = strike2;
                strike2 = temp;
                bowler = nextBowler++;
                if (nextBowler == 11) {
                    nextBowler = 7;
                }
            }
        }
        System.out.println();
        return ballcommentary;
    }
    void printScoreBoard(int team1Id, int team2Id) {
        System.out.println("\nBatting : ");
        System.out.format("\n%-15s%-15s%-15s%-15s%-15s\n", "Player Name", "Runs", "Balls", "4s", "6s");
        for (Integer playerId : teamRepository.getPlayerList(team1Id)) {
            System.out.format("%-15s%-15s%-15s%-15s%-15s\n", playerRepository.getPlayerName(playerId),
                    batsmanRepository.getPlayerScore(playerId,matchId),
                    batsmanRepository.getPlayerPlayedBalls(playerId,matchId), batsmanRepository.getPlayer4s(playerId,matchId), batsmanRepository.getPlayer6s(playerId,matchId));

        }
        System.out.format("\nBowling : \n\n%-15s%-15s%-15s%-15s\n", "Player Name", "Runs", "Balls", "Wickets");
        for (int i = 7; i <= 10; i++) {
            Integer playerId = teamRepository.getPlayerList(team2Id).get(i);
            System.out.format("%-15s%-15s%-15s%-15s\n", playerRepository.getPlayerName(playerId),
                    bowlerRepository.getBowlerGivenRuns(playerId,matchId),
                    bowlerRepository.getBowlerThrownBalls(playerId,matchId),
                    bowlerRepository.getBowlerTakenWickets(playerId,matchId));
        }
    }
    void winner(int team1Id, int team2Id) {
        if (teamRepository.getTeamRunsInMatch(team1Id) < teamRepository.getTeamRunsInMatch(team2Id)) {
            matchResult=teamRepository.getTeamName(team2Id) + " won the match by " + (10 - teamRepository.getTeamWicketsInMatch(team2Id)) + " wickets.";
            System.out.println(matchResult);
        } else if (teamRepository.getTeamRunsInMatch(team1Id) > teamRepository.getTeamRunsInMatch(team2Id)) {
            matchResult=teamRepository.getTeamName(team1Id) + " won the match by " +
                    (teamRepository.getTeamRunsInMatch(team1Id) - teamRepository.getTeamRunsInMatch(team2Id)) + " runs.";
            System.out.println(matchResult);
        } else {
            matchResult="Match Tied.";
            System.out.println(matchResult);
        }
    }
    void printScoreBoard() {
        System.out.println("ScoreBoard");
        System.out.println("\n1st Innings : ");
        System.out.println(teamRepository.getTeamName(team1Id) + "\nScore : " + teamRepository.getTeamRunsInMatch(team1Id) + "/" + teamRepository.getTeamWicketsInMatch(team1Id));
        System.out.println("Overs : " + Over.convertToOvers(teamRepository.getTeamBallsInMatch(team1Id)));
        printScoreBoard(team1Id, team2Id);
        System.out.println("\n2nd Innings : ");
        System.out.println(teamRepository.getTeamName(team2Id) + "\nScore : " + teamRepository.getTeamRunsInMatch(team2Id) + "/" + teamRepository.getTeamWicketsInMatch(team2Id));
        System.out.println("Overs : " + Over.convertToOvers(teamRepository.getTeamBallsInMatch(team2Id)));
        printScoreBoard(team2Id, team1Id);
        System.out.println();
        winner(team1Id, team2Id);
        scoreBoardRepository.saveScoreBoard(team1Id,team2Id,tossResultOutput,matchId,matchResult);
    }
}