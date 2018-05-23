import java.lang.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.Clip;

public class Judgement
{
   public int Judgement(Simfile sim, int notenum, double keypos)
   {
      List<List<String>> timestamps = sim.NotesTime();
      double diff = Math.abs((keypos/1000000.0) - Double.parseDouble(timestamps.get(notenum).get(0)));

      if (diff <= 0.1) // perfect
      {
         return 3;
      }
      else if (diff <= 0.25) // great
      {
         return 2;
      }
      else if (diff <= 0.4) // good
      {
         return 1;
      }
      else // miss
      {
         return 0;
      }
   }
}