import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("serial")
        // kill VSCode errors (does nothing in jGRASP)

/**
 * This class can be externally called from a static function and can run in a separate thread
 */
class RunAudio implements Runnable {
    /**
     * The object containing the audio stream within the RunAudio class.
     */
    private Clip music;

    /**
     * Creates a RunAudio object, takes a Clip object and puts it in a class variable.
     */
    public RunAudio(Clip music) {
        this.music = music;
    }

    /**
     * Starts the audio stream (music) if it is not already running.
     */
    public void run() {
        if (!music.isRunning())
            music.start();
    }
}

/**
 * The panel for the actual gameplay of JMania.
 */
public class PanelPlay extends JPanel implements ActionListener {

    /**
     * The array size of the arrow coordinate arrays.
     * Consequently, this is the max number of arrows which can be in a song.
     */
    private int arraySize = 1000;

    /**
     * The dimensions of the panel as integers.
     */
    private int width, height;

    /**
     * The x-coordinates (horizontal position) for the arrows.
     */
    private int[] x = new int[arraySize];

    /**
     * The y-coordinates (vertical position) for the arrows.
     */
    private int[] y = new int[arraySize];

    /**
     * The number of pixels the y-coordinate changes per tick (each time the timer runs)
     */
    private int velY = 1;

    /**
     * The owner of the JFrame.
     */
    private Window owner;

    /**
     * The path where sprites are located.
     */
    private String arrowPath = Driver.projectPath + "/assets/images/";

    /**
     * The main timer that updates gameplay information such as graphics, timing, judgement, etc.
     */
    private Timer tm = new Timer(1, this);

    /**
     * The thread that plays the audio stream (music).
     */
    private Thread audioThread;

    /**
     * A list of scores separated by each individual arrow.
     * Stored as integers, such that 3 is perfect, 2 is great, 1 is good, and 0 is a miss.
     */
    public List<Integer> scores = new ArrayList<Integer>();

    /**
     * A constant specifying a left arrow in a simfile.
     */
    private final String left = "1000";

    /**
     * A constant specifying a downwards arrow in a simfile.
     */
    private final String down = "0100";

    /**
     * A constant specifying an upwards arrow in a simfile.
     */
    private final String up = "0010";

    /**
     * A constant specifying a right arrow in a simfile.
     */
    private final String right = "0001";

    /**
     * A constant specifying no arrows pressed in a simfile.
     */
    private final String notPressed = "0000";

    /**
     * A string containing the filename for the image of an inactive left arrow.
     */
    private String leftDefault = "inactiveLeft.png";

    /**
     * A string containing the filename for the image of an inactive right arrow.
     */
    private String rightDefault = "inactiveRight.png";

    /**
     * A string containing the filename for the image of an inactive downwards arrow.
     */
    private String downDefault = "inactiveDown.png";

    /**
     * A string containing the filename for the image of an inactive upwards arrow.
     */
    private String upDefault = "inactiveUp.png";

    /**
     * A long value used to store the current time on the computer, to keep track of when to draw a frame.
     * Used in conjunction with temptime to be updated when there is an adequate difference between the two.
     */
    private long milltime = System.currentTimeMillis();

    /**
     * A long value used to store the current time on the computer, to keep track of when to draw a frame.
     * Used in conjunction with milltime to be updated until there is an adequate difference between the two.
     */
    private long temptime;

    /**
     * An integer used to count milliseconds until the progress bar should be updated.
     */
    private int progressBarUpdate = 0;

    /**
     * Used to check if the song has started, and should only turn on once.
     */
    private boolean songstarted = false;

    /**
     * The topmost arrow to be destroyed (as an integer, based on note index).
     */
    private int nextArrow;

    /**
     * A counter determining when to initialize a new arrow.
     */
    private int timeCount;

    /**
     * An integer used as a multiplier for the time to initialize a new arrow.
     */
    private int newArrowTime = 225; 

    /**
     * A string set to the value of the arrow key that is pressed, used to check which arrow should be destroyed.
     */
    private String pressed = notPressed;

    /**
     * The default arrow sprite objects.
     */
    private ImageIcon inactiveLeft, inactiveDown, inactiveUp, inactiveRight;

    /**
     * A list of all the arrows as sprites.
     */
    private ImageIcon active[] = new ImageIcon[arraySize];

    /**
     * The default arrow sprite objects.
     */
    public Simfile smf;

    /**
     * List of times when each note should appear, based on Simfile.NotesTime().
     */
    List<List<String>> timestamp;

    /**
     * The current note which is to appear, based on order of time.
     */
    public int noteindex = 0;

    /**
     * The object used to store the audio file being used.
     */
    public File file;

    /**
     * The audio stream as a Clip object which can be directly played and accessed.
     */
    public Clip music;

    /**
     * The current position of the song in microseconds, updated within the main timer
     */
    private double micro;

    /**
     * The label that displays perfects, greats, goods, and misses.
     */
    private JLabel judgeLabel = new JLabel();

    /**
     * The progress bar which displays how far you are in the song.
     */
    private JProgressBar progressBar = new JProgressBar(0, 100);

    /**
     * The length of the audio stream (music) in microseconds.
     */
    private long currentSongLength;

    /**
     * The actual value determining how far you are in the song.
     */
    private int songProgress;

    /**
     * The panel containing the accuracy and score labels.
     */
    private JPanel statsPanel = new JPanel();

    /**
     * The label which displays how accurately you pressed the arrows, as a percentage.
     */
    private JLabel accuracyLabel = new JLabel();

<<<<<<< HEAD
    /**
     * The label which displays a score based on how accurately and how well you pressed the arrows.
     */
    private JLabel scoreLabel = new JLabel();
=======
    public static Color colorPerfect = new Color(56, 142, 60);
    public Color colorGreat = new Color(245, 124, 0);
    public Color colorGood = new Color(251, 192, 45);
    public Color colorMiss = new Color(211, 47, 47);


>>>>>>> 48bbdd9b46f7d02c35d37fddd1ffdc5f3e121582

    /**
     * A function that adds to the note index variable, in case variables must be static within a function.
     */
    public int addnoteindex()
    {
        noteindex++;
        return 1;
    }

    /**
     * Creates a PanelPlay object to display the actual game.
     */
    public PanelPlay(Window owner, int width, int height, Simfile simfile) {
        initTopLayout();
        initStatsLayout();
        initProgressBar();

        // Catch all other exceptions
        try {
            this.file = new File(simfile.AudioFile());
            if (file.exists()) {
                AudioInputStream stream = AudioSystem.getAudioInputStream(file);
                this.music = AudioSystem.getClip();
                music.open(stream);
                adjustSongVolume();

                currentSongLength = music.getMicrosecondLength();
            }
        }
        /******************************************
         * These are all defined in the Simfile class
         ******************************************/ catch (IOException e) {
            throw new RuntimeException("There was an I/O error reading the audio file.");
        } catch (LineUnavailableException e) {
            throw new RuntimeException("We messed up, no line is available for the sound.");
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(
                    "This type of audio file is unsupported. Only uncompressed formats are supported.");
        }

        // Catch the error thrown by PanelPlay
        try {
            smf = simfile;
            timestamp = smf.NotesTime();
        } catch (Exception f) {
            f.printStackTrace();
        }

        this.owner = owner;
        this.width = width;
        this.height = height;

        y[noteindex] = 800; // starts first arrow at y-coordinate 800 (80 pixels below bottom of screen)

        // key bindings
        int arrowMap = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap imap = this.getInputMap(arrowMap);
        KeyStroke leftKey = KeyStroke.getKeyStroke("LEFT");
        KeyStroke rightKey = KeyStroke.getKeyStroke("RIGHT");
        KeyStroke upKey = KeyStroke.getKeyStroke("UP");
        KeyStroke downKey = KeyStroke.getKeyStroke("DOWN");

        KeyStroke releaseLeft = KeyStroke.getKeyStroke("released LEFT");
        KeyStroke releaseRight = KeyStroke.getKeyStroke("released RIGHT");
        KeyStroke releaseUp = KeyStroke.getKeyStroke("released UP");
        KeyStroke releaseDown = KeyStroke.getKeyStroke("released DOWN");

        imap.put(leftKey, "loadLeft");
        imap.put(rightKey, "loadRight");
        imap.put(downKey, "loadDown");
        imap.put(upKey, "loadUp");

        imap.put(releaseLeft, "releaseLeft");
        imap.put(releaseRight, "releaseRight");
        imap.put(releaseUp, "releaseUp");
        imap.put(releaseDown, "releaseDown");

        ActionMap amap = this.getActionMap();
        amap.put("loadLeft", new ArrowAction(left));
        amap.put("loadRight", new ArrowAction(right));
        amap.put("loadDown", new ArrowAction(down));
        amap.put("loadUp", new ArrowAction(up));

        amap.put("releaseLeft", new ReleaseAction(left));
        amap.put("releaseRight", new ReleaseAction(right));
        amap.put("releaseUp", new ReleaseAction(up));
        amap.put("releaseDown", new ReleaseAction(down));

        owner.requestFocus();
    }

    /**
     * A function that changes the song's volume (loudness)
     */
    private void adjustSongVolume() throws IOException {
        Properties gameProps = new Properties();
        gameProps.load(new FileInputStream(Driver.projectPath + "/app.properties"));
        float songVolume = Float.parseFloat(gameProps.getProperty("songVolume"));

        FloatControl gainControl = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
        float clipRange = gainControl.getMaximum() - gainControl.getMinimum();
        float clipVolStep = clipRange / 100;
        float finalVolume = gainControl.getMinimum() + (clipVolStep*songVolume);
        gainControl.setValue(finalVolume);
    }

    /**
     * Initializes the back button and judgement labels.
     */
    private void initTopLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Driver.bgColor);
        // Add back button to panel
        PanelBack topLayout = new PanelBack();
        topLayout.removeAll();
        topLayout.setLayout(new BoxLayout(topLayout, BoxLayout.X_AXIS));
        topLayout.setBackground(new Color(0, 0, 0, 0)); // make transparent
        topLayout.add(Box.createRigidArea(new Dimension(20, 60)));
        topLayout.add(topLayout.backButton);
        topLayout.backButton.addActionListener(event -> {
            endRound();
//            SwingUtilities.invokeLater(() -> owner.showView(new PanelSelect(owner, Driver.width, Driver.height)));
            SwingUtilities.invokeLater(() -> owner.showView(new PanelEnd(owner, Driver.width, Driver.height, this.calcAccuracy(), this.scores)));

        });
        topLayout.backButton.setText(" Exit");
        topLayout.add(Box.createHorizontalGlue());

        judgeLabel.setFont(Driver.fontBold.deriveFont(28f));

        topLayout.add(judgeLabel);
        topLayout.add(Box.createRigidArea(new Dimension(20, 60)));

        topLayout.setVisible(true);
        add(topLayout);
    }

    /**
     * Stops the game (stops drawing and playing audio)
     */
    private void endRound() {
        // Stops reading the simfile
        tm.stop();
        audioThread.interrupt();
        // Stops playing the song
        songstarted = false;
        music.stop();
        music.flush();
        // Sets the window to show PanelSelect
    }

    /**
     * Initializes the layout for the accuracy and score labels
     */
    private void initStatsLayout() {
        add(Box.createRigidArea(new Dimension(0, 50)));
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(new Color(0,0,0,0));
        Dimension statDimension = new Dimension(Driver.width, 100);

        JPanel accuracyPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 30, 10)); accuracyPanel.setBackground(Driver.bgColor);
        accuracyPanel.setMaximumSize(statDimension);
        accuracyPanel.setBackground(new Color(0,0,0,0));
        accuracyLabel.setText("Accuracy: ");
        accuracyLabel.setFont(Driver.fontRegular.deriveFont(24f));
        accuracyPanel.add(accuracyLabel);

        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0)); scorePanel.setBackground(Driver.bgColor);
        scorePanel.setMaximumSize(statDimension);
        scorePanel.setBackground(new Color(0,0,0,0));
        scoreLabel.setText("Score: ");
        scoreLabel.setFont(Driver.fontRegular.deriveFont(24f));
        scorePanel.add(scoreLabel);

        statsPanel.add(accuracyPanel);
        statsPanel.add(scorePanel);
        add(statsPanel);
    }

    /**
     * Initializes the progress bar on the bottom of the screen, determining how far you have gotten in the song.
     */
    private void initProgressBar() {
        add(Box.createVerticalGlue());

        progressBar.setPreferredSize(new Dimension(this.getWidth(), 20));
        progressBar.setForeground(new Color(243, 66, 53));
        progressBar.setBorder(new LineBorder(Color.BLUE));
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setFont(Driver.standardFont);
        add(progressBar);
    }

    /**
     * A class to handle keypresses and compare to arrows
     */
    private class ArrowAction extends AbstractAction {

        /**
         * A string indicating what arrow key is being pressed
         */
        private String direction;

        /**
         * Creates an ArrowAction object and puts the arrow key direction into a class variable
         */
        public ArrowAction(String direction) {
            this.direction = direction;
        }

        /**
         * Displays what arrow is being pressed (by adding brightness to the judgement arrows) based on direction
         */
        public void actionPerformed(ActionEvent e) {

            switch (direction) {

                case left:
                    leftDefault = "pressedLeft.png";
                    break;
                case right:
                    rightDefault = "pressedRight.png";
                    break;
                case down:
                    downDefault = "pressedDown.png";
                    break;
                case up:
                    upDefault = "pressedUp.png";
                    break;

            }

            pressed = direction;
        }
    }

    /**
     * A class to handle releasing of keypresses and compare to arrows
     */
    private class ReleaseAction extends AbstractAction {

        /**
         * A string indicating what arrow key is being pressed
         */
        private String direction;

        /**
         * Creates an ReleaseAction object and puts the arrow key direction into a class variable
         */
        public ReleaseAction(String direction) {
            this.direction = direction;
        }

        /**
         * Displays what arrow is being pressed (by adding brightness to the judgement arrows) based on direction
         */
        public void actionPerformed(ActionEvent e) {
            switch (direction) {

                case left:
                    leftDefault = "inactiveLeft.png";
                    break;
                case right:
                    rightDefault = "inactiveRight.png";
                    break;
                case down:
                    downDefault = "inactiveDown.png";
                    break;
                case up:
                    upDefault = "inactiveup.png";
                    break;

            }
        }
    }

    /**
     * The function that runs based on the timer.
     * Graphics and music are mostly handled within this function.
     */
    public void actionPerformed(ActionEvent e) {
        timeCount++;

        //Ends game if music finishes playing
        if(!music.isRunning() && music.getMicrosecondPosition() > 0) {
            endRound();
            SwingUtilities.invokeLater(() -> owner.showView(new PanelEnd(owner, Driver.width, Driver.height, this.calcAccuracy(), this.scores)));
        }

        // animate all existing arrows
        for (int start = noteindex; start > 0; start--) {
            y[start] -= velY;
            if (y[start] <= -80 && active[start] != null) {
                int judge = this.Judgement(smf, nextArrow, y[nextArrow]);
                updateResults(judge);
                this.addNote(judge);
                active[start] = null;
                nextArrow++;
            }
        }

        for (int start = 1; start < noteindex; start++) {
            if (active[start] != null) {
                nextArrow = start;
                break;
            }
        }

        // if an arrow key is pressed and the topmost arrow is the corresponding arrow,
        // destroy it.
        if (y[nextArrow] < 720 && active[nextArrow] != null) {
            switch (pressed) {

                case left:
                    if (active[nextArrow].getDescription() == "left") {
                        int judge = this.Judgement(smf, nextArrow, y[nextArrow]);
                        updateResults(judge);
                        if (judge > 0) {
                            active[nextArrow] = null;
                            this.addNote(judge);
                            nextArrow++;
                        }
                    }
                    break;
                case down:
                    if (active[nextArrow].getDescription() == "down") {
                        int judge = this.Judgement(smf, nextArrow, y[nextArrow]);
                        updateResults(judge);
                        if (judge > 0) {
                            active[nextArrow] = null;
                            this.addNote(judge);
                            nextArrow++;
                        }
                    }
                    break;
                case up:
                    if (active[nextArrow].getDescription() == "up") {
                        int judge = this.Judgement(smf, nextArrow, y[nextArrow]);
                        updateResults(judge);
                        if (judge > 0) {
                            active[nextArrow] = null;
                            this.addNote(judge);
                            nextArrow++;
                        }
                    }
                    break;
                case right:
                    if (active[nextArrow].getDescription() == "right") {
                        int judge = this.Judgement(smf, nextArrow, y[nextArrow]);
                        updateResults(judge);
                        if (judge > 0) {
                            active[nextArrow] = null;
                            this.addNote(judge);
                            nextArrow++;
                        }
                    }
                    break;
                case notPressed:
                    break;
            }
            pressed = notPressed;
        }
        temptime = System.currentTimeMillis();

        if (temptime - milltime >= 16) {
            if (progressBarUpdate >= 32) { // Runs every ~0.5 seconds
                // Updates the progress bar 
                songProgress = (int) (micro / this.currentSongLength * 100);
                progressBar.setValue(songProgress);
                progressBarUpdate = 0;
            } else {
                progressBarUpdate++;
            }
            repaint();
            milltime = temptime;
        }

        // redraw the canvas after all is said and done

        if (!songstarted) {
            audioThread = new Thread(new RunAudio(music));
            audioThread.start();
            songstarted = true;
        }

        micro = (double) music.getMicrosecondPosition();
        double second = micro / 1000000;

        if (noteindex < smf.NoteCount()) { // so we don't run out of notes
            if (second > Double.parseDouble(timestamp.get(noteindex).get(0)) - 1.0) {
                loadArrow(timestamp.get(noteindex).get(1));
                y[noteindex] = 800;

                addnoteindex();
                // System.out.println(Double.toString(second));
                // System.out.println(timestamp.get(noteindex).get(0));
            }
        }

    }

    /**
     * The function that handles drawing the graphics on to the screen.
     */
    public void paintComponent(Graphics g) {

        // background
        super.paintComponent(g);
        g.setColor(Driver.bgColor);
        g.fillRect(0, 0, width, height);

        loadStaticSprites(g); // draw default arrows

        // redraw every arrow with their updated y-coord values (the real animation)
        if (noteindex >= 1) {
            for (int start = noteindex; start > 0; start--) {
                if (active[start] != null) {
                    g.drawImage(active[start].getImage(), x[start], y[start], null, null);
                }
            }
        }

        tm.start(); // start animation
    }

    /**
     * Loads images of arrows into ImageIcon variables and draws them on the screen.
     */
    private void loadStaticSprites(Graphics g) {

        inactiveDown = new ImageIcon(arrowPath + downDefault);
        inactiveLeft = new ImageIcon(arrowPath + leftDefault);
        inactiveUp = new ImageIcon(arrowPath + upDefault);
        inactiveRight = new ImageIcon(arrowPath + rightDefault);

        g.drawImage(inactiveLeft.getImage(), 425, 100, null, null);
        g.drawImage(inactiveDown.getImage(), 525, 100, null, null);
        g.drawImage(inactiveUp.getImage(), 625, 100, null, null);
        g.drawImage(inactiveRight.getImage(), 725, 100, null, null);

    }

    /**
     * Loads new arrows on the screen based on the direction that it should be in (which is passed into it).
     */
    private void loadArrow(String direction) {
        switch (direction) {

            case left:
                active[noteindex] = new ImageIcon(arrowPath + "activeLeft.png");
                active[noteindex].setDescription("left");
                x[noteindex] = 425;
                break;

            case down:
                active[noteindex] = new ImageIcon(arrowPath + "activeDown.png");
                active[noteindex].setDescription("down");
                x[noteindex] = 525;
                break;

            case up:
                active[noteindex] = new ImageIcon(arrowPath + "activeUp.png");
                active[noteindex].setDescription("up");
                x[noteindex] = 625;
                break;

            case right:
                active[noteindex] = new ImageIcon(arrowPath + "activeRight.png");
                active[noteindex].setDescription("right");
                x[noteindex] = 725;
                break;

            case notPressed:
                break;

        }
    }

    /**
     * Determines how accurate the pressed arrow is compared to when it should be pressed (through y-coordinates of arrows).
     * Returns 3 for perfect, 2 for great, 1 for good, and 0 for a miss.
     */
    public int Judgement(Simfile sim, int notenum, int y) {
        int diff = Math.abs(y - 80); // get difference between the y-coordinate positions when pressed and the actual arrow on screen

        if (diff <= 30) // perfect
        {
            return 3;
        } else if (diff <= 60) // great
        {
            return 2;
        } else if (diff <= 90) // good
        {
            return 1;
        } else // miss
        {
            return 0;
        }
    }

    /**
     * Calculates the score by finding the sum of the Judgement scores.
     */
    public int calcScore() {
        int totalscore = 0;
        for (Integer s : scores)
            totalscore += s;

        return totalscore;
    }

    /**
     * Calculates the accuracy by dividing calcScore() by the maximum possible score (the amount of arrows multiplied by 3, as 3 is perfect).
     * Returns a double which is a percentage.
     */
    public double calcAccuracy() {
        int totalscore = calcScore();

        return (double) totalscore / ((double) this.scores.size() * 3.0);
    }

    /**
     * Adds an arrow's score to the total list of scores.
     */
    public void addNote(int score) {
        scores.add(score);
    }

    /**
     * Updates the judgement text, displaying whether the player got a perfect, great, good, or miss on the most recently pressed arrow.
     */
    public void updateResults(int judge) {
        switch (judge) {
            case 3:
                judgeLabel.setText("PERFECT");
                judgeLabel.setForeground(colorPerfect);
                break;
            case 2:
                judgeLabel.setText("GREAT");
                judgeLabel.setForeground(colorGreat);
                break;
            case 1:
                judgeLabel.setText("GOOD");
                judgeLabel.setForeground(colorGood);
                break;
            case 0:
                judgeLabel.setText("MISS");
                judgeLabel.setForeground(colorMiss);
                break;

        }
        double roundedAccuracy = Math.round(this.calcAccuracy() * 10000.0) / 100.0;
        accuracyLabel.setText("Accuracy: " + roundedAccuracy + "%");
        scoreLabel.setText("Score: " + this.calcScore());
    }

}
