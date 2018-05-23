// as of tuesday, this is the most recent version

/**
 * Short for simulation file.
 * Reads and parses .csm files
 */

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

   /**
    * The filename is an argument taken while making a new object.
    * It can be publically called using Simfile.filename
    */
   public String filename;
   
   private BufferedReader sf;
   private List<String> simlist = new ArrayList<String>();
   private String[] sim; 
   
   private String[] bpms;
   
   private String line;
   private String notestr;
   private List<String> noteslist = new ArrayList<String>();
   private List<List<String>> note = new ArrayList<List<String>>();

   private List<List<String>> note1 = new ArrayList<List<String>>();
   
   private double[][] a;
   private Matcher m;

   private double globaloffset;
   
   private double offset;
   
   public Simfile(String filename, double globaloffset) throws Exception
   {
      this.globaloffset = globaloffset;
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

    public String Genre()
    {
        for (int i = 0; i < sim.length; i++)
        {
            if (sim[i].contains("#GENRE:"))
            {
                return sim[i].replaceAll("#GENRE:","").replaceAll("\n","").replaceAll(";","");
            }
        }
        return "none";
    }

    public int Year()
    {
        for (int i = 0; i < sim.length; i++)
        {
            if (sim[i].contains("#YEAR:"))
            {
                return Integer.parseInt(sim[i].replaceAll("#YEAR:","").replaceAll("\n","").replaceAll(";",""));
            }
        }
        return 0;
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
      note = new ArrayList<List<String>>();
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

   public int NoteCount()
   {
      int count = 0;
      for (List<String> sub : this.Notes())
      {
         count += sub.size();
      }
      
      return count;
   }
   
   public List<List<String>> NotesTime()
   {
      offset = this.Offset();
      note1 = this.Notes();
      
      double[][] bpmarray = this.BPM();
      int bpmarraysize = bpmarray.length;
      int bpmindex = 0;
      double currentBPM = bpmarray[0][1];
      double currentBPS = currentBPM/60;
      double currentBPuS = currentBPS/1000000;

      List<List<String>> notestime = new ArrayList<List<String>>();
      
      double ts = offset+globaloffset;
      
      for (int i = 0; i < note1.size(); i++)
      {
         for (int a = 0; a < note1.get(i).size(); a++) // we have to evaluate BPM in real time when adding stuff so maybe add a while loop or something in here
         {
            List<String> tmp = new ArrayList<String>();
            tmp.add(Double.toString(ts));
            tmp.add(note1.get(i).get(a));
            notestime.add(tmp);
            ts += 1/currentBPS/note1.get(i).size()*4; ///0.896
            if (bpmindex < bpmarraysize-1 && bpmindex+1 <= bpmarraysize-1)                        
            {
               if (i >= bpmarray[bpmindex+1][0]) // be careful of >= idk what will happen
               {
                  System.out.println("WARNING: BPM HAS CHANGED");
                  currentBPM = bpmarray[bpmindex+1][1];
                  currentBPS = currentBPM/60;
                  currentBPuS = currentBPS/1000000;
                  bpmindex++;
               }
            }
         }
      }
      return notestime;
   }
}