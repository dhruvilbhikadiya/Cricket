package tekion.assignment2.Controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;
import tekion.assignment2.Repository.CommentaryRepository;

@RestController
@ResponseBody
@RequestMapping(path="/commentary",
        produces="application/json")
public class CommentaryController {

    CommentaryRepository commentaryRepository=new CommentaryRepository();
    @GetMapping("/match/{matchId}/innings/{inningsId}/ball/{ballnumber}")
    public Document getBallCommentary(@PathVariable("matchId")int matchId, @PathVariable("inningsId") int inningsId, @PathVariable("ballnumber") int ball)
    {

        Document doc=commentaryRepository.getMatchCommentary(matchId);
        Document doc1;
        if(inningsId==1)
        doc1=(Document) doc.get("Innings1");
        else
            doc1=(Document) doc.get("Innings2");
        String s="ball"+ball;
        Document doc2 = (Document) doc1.get(s);
        return doc2;
    }
}
