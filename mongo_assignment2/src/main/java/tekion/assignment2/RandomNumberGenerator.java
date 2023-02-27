package tekion.assignment2;

import org.springframework.stereotype.Service;

@Service
public class RandomNumberGenerator
{
    public int generateRandomNumber(int range)
    {
        return (int)(Math.random()*(range+1)) ;
    }
}
