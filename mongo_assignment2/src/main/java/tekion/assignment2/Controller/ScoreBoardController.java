package tekion.assignment2.Controller;

import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tekion.assignment2.Repository.ScoreBoardRepository;

@Controller
@ResponseBody
@RequestMapping(path="/scoreboard",
        produces="application/json")
public class ScoreBoardController {
    ScoreBoardRepository scoreBoardRepository=new ScoreBoardRepository();
    @GetMapping("/get/match/{matchId}")
     public Document getScoreBoardOfMatch(@PathVariable("matchId") int matchId)
    {
        return scoreBoardRepository.getScoreBoard(matchId);
    }
}
