package tekion.assignment2.Controller;

import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tekion.assignment2.CreateTeam;
import tekion.assignment2.Repository.TeamRepository;

@Controller
@ResponseBody
@RequestMapping(path="/team",
        produces="application/json")
public class MatchController {
    CreateTeam createTeam=new CreateTeam();
    TeamRepository teamRepository=new TeamRepository();
    @GetMapping("/get/{teamId}")
    public Document getTeamDetails(@PathVariable("teamId") int teamId)
    {
        System.out.println(teamId);
        return teamRepository.getTeam(teamId);
    }
    @PostMapping("/addTeam/{teamDetails}")
    public void addTeamDetails(@PathVariable("teamDetails") String teamDetails)
    {
        createTeam.addTeam(teamDetails);
    }
}
