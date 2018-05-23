import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Accuracy
{
   public List<Integer> scores;

   public int calcScore()
   {
      int totalscore = 0;
      for (Integer s:scores)
         totalscore += s;
      
      return totalscore;
   }

   public double calcAccuracy()
   {
      int totalscore = calcScore();

      return (double)totalscore / (double)this.scores.size()*3.0;
   }

   public void addNote(int score)
   {
      scores.add(score);
   }
}