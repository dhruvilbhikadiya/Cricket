package tekion.assignment2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tekion.assignment2.Connection.ConnectMongo;
import tekion.assignment2.Repository.TeamRepository;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.gte;
@Service
public class Cricket {

    private TeamRepository teamRepository =new TeamRepository() ;

    private PlayMatch playingMatch=new PlayMatch();
    public void begin() throws FileNotFoundException {
        System.out.println("we are in cricket");
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        ((Logger)rootLogger).setLevel(Level.OFF);

        Scanner sc = new Scanner(System.in);

        MongoDatabase database = ConnectMongo.getConnection() ;
        //finding the number of  already existing teams
        MongoCollection<Document> collection1 = database.getCollection("teams") ;
        List<Document> team = collection1.find(gte("teamId",0)).limit(1).into(new ArrayList<>());
        int teamsSize = (team.size()==0) ? 0 : (int)team.get(0).get("teamId")+1 ;
        if(teamsSize<4)
        {
            String URL = "//Users//bdmanjibhai//Downloads//Ass.txt" ;
            CreateTeam teams = new CreateTeam();
            teams.createTeam(URL);
            team = collection1.find(gte("teamId",0)).limit(1).into(new ArrayList<>());
            teamsSize = (team.size()==0) ? 0 : (int)team.get(0).get("teamId")+1 ;
        }
        teamsSize-- ;
        RandomNumberGenerator rg = new RandomNumberGenerator();

    //starting match
        char playMoreMatch;
        do {
            int team1_id = rg.generateRandomNumber(teamsSize);
            int team2_id = rg.generateRandomNumber(teamsSize);
            while (team1_id == team2_id) {
                team2_id = rg.generateRandomNumber(teamsSize);
            }
            if(team1_id>team2_id)
            {
                int temp=team2_id;
                team2_id=team1_id;
                team1_id=temp;
            }
            teamRepository.clearData(team1_id) ;
            teamRepository.clearData(team2_id) ;
            System.out.println(team1_id + " " + team2_id);

            System.out.println("\nLet's Begin  " + teamRepository.getTeamName(team1_id) + " v/s " + teamRepository.getTeamName(team2_id));

            //call to create instances to store player details for each match
            playingMatch.playMatch(team1_id, team2_id);
//            playingMatch.createMatchdata();
//            playingMatch.toss();
//            playingMatch.startMatch();
//            playingMatch.printScoreBoard();
//            teamRepository.clearData(team1_id) ;
//            teamRepository.clearData(team2_id) ;
           //playingMatch.matchId++;
            System.out.print("\nPlay More Match (y/n) : ");
            playMoreMatch = sc.next().charAt(0);

        } while (playMoreMatch == 'y');

    }
}