import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Short for simulation file.
 * Reads, parses, and stores data for .csm files.
 */
public class Simfile {

    /**
     * The filename of the simfile being accessed.
     */
    public String filename;

    /**
     * The object which contains the simfile.
     */
    private BufferedReader sf;

    /**
     * A list of Strings containing each line in the simfile.
     */
    private List<String> simlist = new ArrayList<String>();

    /**
     * An array of Strings containing each line in the simfile. This is the variable used in methods outside the constructor.
     */
    private String[] sim;

    /**
     * An array of Strings containing each BPM in the simfile along with the time in beats in which they occur.
     */
    private String[] bpms;

    /**
     * A variable used to temporarily store each line to put in the simlist array.
     */
    private String line;

    /**
     * A string containing all the notes in a simfile.
     */
    private String notestr;

    /**
     * A list of strings containing all the notes in a simfile, split by beat.
     */
    private List<String> noteslist = new ArrayList<String>();

    /**
     * A list of lists of strings containing all the notes in a simfile in strings, split by beat.
     */
    private List<List<String>> note = new ArrayList<List<String>>();

    /**
     * An array of Strings containing each BPM in the simfile along with the time in beats in which they occur.
     */
    private List<List<String>> note1 = new ArrayList<List<String>>();

    /**
     * An array of doubles containing each BPM in the simfile along with the time in beats in which they occur.
     */
    private double[][] a;

    /**
     * This matches the notes into a regular expression which splits the series of notes into groups of four numbers.
     */
    private Matcher m;

    /**
     * A double indicating how delayed the arrows should be in comparison to any song being played.
     */
    private double globaloffset;

    /**
     * A double indicating how delayed the arrows should be in comparison to the specified song in the simfile.
     */
    private double offset;

    /**
     * Create a Simfile object, given a simfile filename (.csm) and an offset in seconds.
     * The offset should only be 0.0 unless the game starts too slow or too fast on every song.
     *
     * @param filename The filename of the .csm file
     * @param globaloffset The number of milliseconds to offset the reading of the file by
     * @throws Exception Throws a FileNotFoundException in case the .csm is not found
     */
    public Simfile(String filename, double globaloffset) throws Exception {
        this.globaloffset = globaloffset;
        sf = new BufferedReader(new FileReader(filename));
        while ((line = sf.readLine()) != null) { // takes file and put each line into a list
            simlist.add(line);
        }
        sim = simlist.toArray(new String[0]); // converts mutable list into immutable array
    }

    /**
     * Returns the title (name) of the song as a String, which is specified in the simfile.
     * If there is no title specified, it will return "none".
     * @return The title of the song
     */
    public String Title() {
        for (int i = 0; i < sim.length; i++) {
            if (sim[i].contains("#TITLE:")) { // searches each line for title, then strips line of unnecessary characters
                return sim[i].replaceAll("#TITLE:", "").replaceAll("\n", "").replaceAll(";", "");
            }
        }
        return "none";
    }

    /**
     * Returns the subtitle of the song as a String, which is specified in the simfile.
     * This may contain relevant information that may not be in the actual song title.
     * If there is no subtitle specified, it will return "none".
     * @return The subtitle for the song
     */
    public String Subtitle() {
        for (int i = 0; i < sim.length; i++) {
            if (sim[i].contains("#SUBTITLE:")) { // searches each line for subtitle, then strips line of unnecessary characters
                return sim[i].replaceAll("#SUBTITLE:", "").replaceAll("\n", "").replaceAll(";", "");
            }
        }
        return "none";
    }

    /**
     * Returns the artist of the song as a String, which is specified in the simfile.
     * If there is no artist specified, it will return "none".
     * @return The name of the artist
     */
    public String Artist() {
        for (int i = 0; i < sim.length; i++) {
            if (sim[i].contains("#ARTIST:")) { // searches each line for artist, then strips line of unnecessary characters
                return sim[i].replaceAll("#ARTIST:", "").replaceAll("\n", "").replaceAll(";", "");
            }
        }
        return "none";
    }

    /**
     * Returns the genre of the song as a String, which is specified in the simfile.
     * If there is no genre specified, it will return "none".
     * @return The genre(s) of the song
     */
    public String Genre() {
        for (int i = 0; i < sim.length; i++) {
            if (sim[i].contains("#GENRE:")) { // searches each line for genre, then strips line of unnecessary characters
                return sim[i].replaceAll("#GENRE:", "").replaceAll("\n", "").replaceAll(";", "");
            }
        }
        return "none";
    }

    /**
     * Returns the year of the song as an integer, which is specified in the simfile.
     * If there is no year specified, it will return 0.
     * @return The year the song was released
     */
    public int Year() {
        for (int i = 0; i < sim.length; i++) {
            if (sim[i].contains("#YEAR:")) { // searches each line for year, then strips line of unnecessary characters
                return Integer.parseInt(sim[i].replaceAll("#YEAR:", "").replaceAll("\n", "").replaceAll(";", ""));
            }
        }
        return 0;
    }

    /**
     * Returns the audio file as a String, which is specified in the simfile.
     * If there is no audio file specified, it will return "none".
     * @return The name of the .wav file that this Simfile corresponds to
     */
    public String AudioFile() {
        for (int i = 0; i < sim.length; i++) {
            if (sim[i].contains("#MUSIC:")) { // searches each line for audio file, then strips line of unnecessary characters and adds directory
                String temp = sim[i].replaceAll("#MUSIC:", "").replaceAll("\n", "").replaceAll(";", "");
                return ("assets/songs/" + (temp.substring(0, temp.length() - 4)) + "/" + temp);
            }
        }
        return "none";
    }

    /**
     * Returns the BPM (beats per minute) of the song as a two-dimensional double array, which is specified in the simfile.
     * The number of beats into the song is the first double in each array, followed by the actual BPM.
     * If there is no BPM specified, it will return a blank array.
     * @return A 2D array consisting of all BPMs (and changes if any) and their position in the song
     */
    public double[][] BPM() {
        for (int i = 0; i < sim.length; i++) {
            if (sim[i].contains("#BPMS:")) { // searches each line for BPM, then strips line of unnecessary characters and splits values into arrays
                bpms = sim[i].replaceAll("#BPMS:", "").replaceAll("\n", "").replaceAll(";", "").split(",");
                double[][] bpmarray = new double[bpms.length][2];
                for (int a = 0; a < bpms.length; a++) {
                    for (int b = 0; b < 2; b++) {
                        bpmarray[a][b] = Double.parseDouble(bpms[a].split("=")[b]);
                    }
                }
                return bpmarray;
            }
        }
        return a;
    }

    /**
     * Returns the offset (delay) of the song in seconds as an integer, which is specified in the simfile.
     * If there is no offset specified, it will return 0.
     * @return The amount of seconds that the song should be offset by
     */
    public double Offset() {
        for (int i = 0; i < sim.length; i++) {
            if (sim[i].contains("#OFFSET:")) { // searches each line for offset, then strips line of unnecessary characters
                return Double.parseDouble(sim[i].replaceAll("#OFFSET:", "").replaceAll("\n", "").replaceAll(";", ""));
            }
        }
        return 0;
    }

    /**
     * Returns the notes (arrows) of the song as a mutable two-dimensional list of Strings, which are specified in the simfile.
     * The strings consist of four zeroes and ones, with the numbers corresponding to left, down, up, and right respectively.
     * For example, 0100 is up, and 0001 is right.
     * Each list of strings inside the main list is split by beat, meaning that if there are 8 strings in an array, the notes are split into 1/8 beats.
     * If there are no notes in the simfile, a blank array will be returned.
     * @return A 2D list of all notes in the song
     */
    public List<List<String>> Notes() {
        note = new ArrayList<List<String>>();
        for (int i = 0; i < sim.length; i++) {
            if (sim[i].contains("#NOTES:")) {
                notestr = ""; // first add all the stuff after #NOTES into one string
                for (int a = i + 1; a < sim.length; a++) {
                    notestr += sim[a].replaceAll(";", "");
                }
                noteslist = Arrays.asList(notestr.split(",")); // then split by commas
                Pattern p = Pattern.compile(".{1,4}");
                for (int b = 0; b < noteslist.size(); b++) { // now there is an array of strings of numbers with newlines 
                    m = p.matcher(noteslist.get(b)); // use regular expressions to find the notes
                    note.add(new ArrayList<>());
                    while (m.find()) {
                        note.get(b).add(m.group()); // split them into an array inside the other array for each measure
                    }
                }
                return note;
            }
        }
        return note;
    }

    /**
     * Returns the number of notes in the song's simfile as an integer.
     * @return The number of total notes in the song
     */
    public int NoteCount() {
        int count = 0;
        for (List<String> sub : this.Notes()) {
            count += sub.size();
        }

        return count;
    }

    /**
     * Returns the notes (arrows) of the song along with the times they should occur within the song in seconds
     * These are returned as a mutable two-dimensional list of Strings, and are specified in the simfile.
     * The strings are the same as the Notes() function for arrows, and the times are doubles converted into String objects.
     * If there are no notes in the simfile, a blank array will be returned.
     * @return A 2D list of which notes should occur in each measure
     */
    public List<List<String>> NotesTime() {
        offset = this.Offset();
        note1 = this.Notes();

        double[][] bpmarray = this.BPM();
        int bpmarraysize = bpmarray.length;
        int bpmindex = 0;
        double currentBPM = bpmarray[0][1];
        double currentBPS = currentBPM / 60;
        double currentBPuS = currentBPS / 1000000;

        List<List<String>> notestime = new ArrayList<List<String>>();

        double ts = offset + globaloffset;

        for (int i = 0; i < note1.size(); i++) // for all the beats in the song
        {
            for (int a = 0; a < note1.get(i).size(); a++) // for each note in a beat
            {
                List<String> tmp = new ArrayList<String>(); // make a temporary list with the current time and note
                tmp.add(Double.toString(ts));
                tmp.add(note1.get(i).get(a));
                notestime.add(tmp); // add this list to the main list
                ts += 1 / currentBPS / note1.get(i).size() * 4; // add the amount of time for the next note to the current time
                if (bpmindex < bpmarraysize - 1 && bpmindex + 1 <= bpmarraysize - 1) {
                    if (i >= bpmarray[bpmindex + 1][0]) // changes BPM if a BPM should happen after a certain beat
                    {
                        currentBPM = bpmarray[bpmindex + 1][1];
                        currentBPS = currentBPM / 60;
                        currentBPuS = currentBPS / 1000000;
                        bpmindex++;
                    }
                }
            }
        }
        return notestime;
    }
}