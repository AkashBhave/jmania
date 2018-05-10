import java.util.Arrays;

public class Simtest
{
   public static void main(String[] args) throws Exception
   {
      Simfile smf = new Simfile("colorful.csm");
      System.out.println(smf.Title());
      System.out.println(smf.Subtitle());
      System.out.println(smf.Artist());
      System.out.println(smf.Offset());
      System.out.println(Arrays.deepToString(smf.BPM()));
      System.out.println(smf.AudioFile());
      System.out.println(smf.Notes());
   }
}