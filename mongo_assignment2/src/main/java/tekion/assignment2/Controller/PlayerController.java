package tekion.assignment2.Controller;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tekion.assignment2.Repository.BatsmanRepository;
import tekion.assignment2.Repository.BowlerRepository;
import tekion.assignment2.Repository.PlayerRepository;

@Controller
@ResponseBody
@RequestMapping(path="/player",
        produces="application/json")
public class PlayerController {
    BatsmanRepository batsmanRepository=new BatsmanRepository();
    BowlerRepository bowlerRepository=new BowlerRepository();
    PlayerRepository playerRepository=new PlayerRepository();
    @GetMapping("/get/{playerId}")
    public Document getPlayerData(@PathVariable("playerId") int playerId)
    {
        Document player=playerRepository.getPlayer(playerId);
        return player;
    }
//    player.append("playerId",playerId)
//            .append("playerName",s[i])
//                    .append("playerType",type)
//                    .append("playerTeam",s[0])
//                    .append("playerTotalScore",0)
//                    .append("playerTotal4s",0)
//                    .append("playerTotal6s",0)
//                    .append("playerTotalPlayedBalls",0)
//                    .append("playerTotalOut",0) ;
//
//            if(i>7)
//    {
//        player.append("bowlerTotalTakenWickets",0)
//                .append("bowlerTotalThrownBalls",0)
//                .append("bowlerTotalGivenRuns",0);
//
//    }
    @GetMapping("/get/statistics/{playerId}")
    public Document getPlayerStatistics(@PathVariable("playerId") int playerId)
    {
        Document player=playerRepository.getPlayer(playerId);
        Document stats=new Document();
        int runs_scored= (int) player.get("playerTotalScore");
        int balls_faced=(int) player.get("playerTotalPlayedBalls");
        double strikeRate= (runs_scored / (balls_faced*1.0)) * 100;
       int total_out=(int) player.get("playerTotalOut");
       if(total_out==0)
           total_out=1;
       double average=(runs_scored/(total_out*1.0));
        stats.append("playerId",player.get("playerId"))
                .append("playerName",player.get("playerName"))
                .append("playerTotalPlayedBalls",player.get("playerTotalPlayedBalls"))
                .append("playerTotalScore",player.get("playerTotalScore"))
                .append("playerStrikeRate",strikeRate)
                .append("playerAverage",average);
        return stats;
    }
    @GetMapping("/get/match/{matchId}/player/{playerId}")
    public Document getPlayerDataInMatch(@PathVariable("matchId") int matchId, @PathVariable("playerId") int playerId)
    {
        Document batsman=batsmanRepository.getPlayer(playerId,matchId);
        Document player=new Document("_id",new ObjectId());
        player.append("playername",playerRepository.getPlayerName(playerId))
                .append("playerScoreInmatch",batsman.get("batsmanScore"))
                .append("playerPlayedBalls",batsman.get("batsmanPlayedBalls"))
              .append("player4sInmatch",batsman.get("batsman4s"))
              .append("player6sInmatch",batsman.get("batsman6s"));
        Document bowler=bowlerRepository.getPlayer(playerId,matchId);
        if(bowler!=null)
        {
            player.append("playerTakenWickets",bowler.get("bowlerTakenWickets"))
                    .append("playerThrownBalls",bowler.get("bowlerThrownBalls"))
                    .append("playerGivenRuns",bowler.get("bowlerGivenRuns"));
        }
           // batsman.append("Bowler",bowler);
        return player;
    }
    @GetMapping("/get/match/{matchId}/batsman/{batsmanId}")
    public Document getBatsmanDataInMatch(@PathVariable("matchId") int matchId,@PathVariable("batsmanId")int batsmanId)
    {
        Document batsman=batsmanRepository.getPlayer(batsmanId,matchId);
        return batsman;
    }
    @GetMapping("/get/match/{matchId}/bowler/{bowlerId}")
    public Document getBowlerDataInMatch(@PathVariable("matchId") int matchId,@PathVariable("bowlerId")int bowlerId)
    {
        Document bowler=bowlerRepository.getPlayer(bowlerId,matchId);
        return bowler;
    }
}
