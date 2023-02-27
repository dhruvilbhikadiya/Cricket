package tekion.assignment2;

import org.springframework.stereotype.Service;

@Service
public class Over
{
    public static String convertToOvers(int balls)
    {
        String answer ;
        int overs = balls/6 ;
        int overBalls = balls%6 ;
        answer = overs + "." + overBalls ;
        return answer ;
    }
}
