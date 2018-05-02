import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Simfile
{

   public String filename;
   private BufferedReader sf;
   private List<String> simlist = new ArrayList<String>();
   private String[] sim; 
   private String[] bpms;
   private String line;
   private String notestr;
   private List<String> noteslist = new ArrayList<String>();
   private List<List<String>> note = new ArrayList<List<String>>();
   private double[][] a;
   private Matcher m;
   
   public Simfile(String filename) throws Exception
   {
      sf = new BufferedReader(new FileReader(filename));
      while ((line = sf.readLine()) != null)
      {
         simlist.add(line);
      }
      sim = simlist.toArray(new String[0]);
   }
        
   public String Title()
   {
      for (int i = 0; i < sim.length; i++)
      {
         if (sim[i].contains("#TITLE:"))
         {
            return sim[i].replaceAll("#TITLE:","").replaceAll("\n","").replaceAll(";","");
         }
      }
      return "none";
   }
               
   public String Subtitle()
   {
      for (int i = 0; i < sim.length; i++)
      {
         if (sim[i].contains("#SUBTITLE:"))
         {
            return sim[i].replaceAll("#SUBTITLE:","").replaceAll("\n","").replaceAll(";","");
         }
      }
      return "none";
   }
         
   public String Artist()
   {
      for (int i = 0; i < sim.length; i++)
      {
         if (sim[i].contains("#ARTIST:"))
         {
            return sim[i].replaceAll("#ARTIST:","").replaceAll("\n","").replaceAll(";","");
         }
      }
      return "none";
   }
                         
   public String AudioFile()
   {
      for (int i = 0; i < sim.length; i++)
      {
         if (sim[i].contains("#MUSIC:"))
         {
            return sim[i].replaceAll("#MUSIC:","").replaceAll("\n","").replaceAll(";","");
         }
      }
      return "none";
   }
            
   public double[][] BPM()
   {
      for (int i = 0; i < sim.length; i++)
      {
         if (sim[i].contains("#BPMS:"))
         {
            bpms = sim[i].replaceAll("#BPMS:","").replaceAll("\n","").replaceAll(";","").split(",");
            double[][] bpmarray = new double[bpms.length][2];
            for (int a = 0; a < bpms.length; a++)
            {
               for (int b = 0; b < 2; b++)
               {
                  bpmarray[a][b] = Double.parseDouble(bpms[a].split("=")[b]);
               }
            }
            return bpmarray;
         }
      }
      return a;
   }
            
   public double Offset()
   {
      for (int i = 0; i < sim.length; i++)
      {
         if (sim[i].contains("#OFFSET:"))
         {
            return Double.parseDouble(sim[i].replaceAll("#OFFSET:","").replaceAll("\n","").replaceAll(";",""));
         }
      }
      return 0;
   }
            
   public List<List<String>> Notes()
   {
      for (int i = 0; i < sim.length; i++)
      {
         if (sim[i].contains("#NOTES:"))
         {
            notestr = ""; // first you add all the stuff after #NOTES into one string
            for (int a = i+1; a < sim.length; a++)
            {
               notestr += sim[a].replaceAll(";","");
            }
            noteslist = Arrays.asList(notestr.split(",")); // then split by commas
            Pattern p = Pattern.compile(".{1,4}");
            for (int b = 0; b < noteslist.size(); b++) // now you have an array of newline strings of numbers so you need to split them into an array inside the other array for each measure
            {
               m = p.matcher(noteslist.get(b));
               note.add(new ArrayList<>());
               while (m.find())
               {
                  note.get(b).add(m.group());
               }
            }
            return note;
         }
      }
      return note;
   }
}