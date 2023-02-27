package tekion.assignment2.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tekion.assignment2.CreateTeam;
import tekion.assignment2.Cricket;
import tekion.assignment2.Over;
import tekion.assignment2.PlayMatch;

import java.io.FileNotFoundException;
import java.util.Optional;

@RestController
@ResponseBody
@RequestMapping(path="/api/startMatch",
        produces="application/json")
@CrossOrigin(origins="http://localhost:8080")
public class StartController {
    Cricket cricket=new Cricket();
    PlayMatch playMatch=new PlayMatch();
  @GetMapping("/")
    public void startMatch  ()
    {

        try {
            cricket.begin();
           // createTeam.createTeam("//Users//bdmanjibhai//Downloads//Ass.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/team1/{Team1Id}/team2/{Team2Id}")
    public void startMatchWithId (@PathVariable("Team1Id") int Team1Id,@PathVariable("Team2Id") int Team2Id)
    {
            playMatch.playMatch(Team1Id,Team2Id);
            // createTeam.createTeam("//Users//bdmanjibhai//Downloads//Ass.txt");
    }

}
