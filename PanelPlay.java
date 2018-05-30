// blah blah blah imports
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.net.URL;
import java.text.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial") // kill VSCode errors (does nothing in jGRASP)

// class to begin song playback
class RunAudio implements Runnable {
    private Clip music;

    public RunAudio(Clip music) {
        this.music = music;
    }

    public void run() {
        if (!music.isRunning())
            music.start();
    }
}

public class PanelPlay extends JPanel implements ActionListener {

    private int arraySize = 1000; // max number of arrows
    private int width, height;
    private int[] x = new int[arraySize]; // list of arrow x-coords
    private int[] y = new int[arraySize]; // list of arrow y-coords
    private int velY = 1; // speed of each arrow (not completely true, speed is also effected by timer
                          // delay)
    private Window owner; // jframe owner
    private String arrowPath = Driver.projectPath + "/assets/images/"; // sprite path
    private Timer tm = new Timer(1, this); // animation timer
    private Thread audioThread;

    public List<Integer> scores = new ArrayList<Integer>(); // list of scores

    // variable names, interpretation of .csm notes
    private final String left = "1000";
    private final String down = "0100";
    private final String up = "0010";
    private final String right = "0001";
    private final String notPressed = "0000";

    // inactive sprites, used to switch when key pressed
    private String leftDefault = "inactiveLeft.png";
    private String rightDefault = "inactiveRight.png";
    private String downDefault = "inactiveDown.png";
    private String upDefault = "inactiveUp.png";

    private long temptime; // basically a stopwatch placeholder, used to collaborate with swing's 60 fps
                           // cap
    private long milltime = System.currentTimeMillis(); // actual stopwatch
    private boolean songstarted = false; // used to only start song once

    private int nextArrow; // the topmost arrow to be destroyed
    private int timeCount; // counter for when to initialize a new arrow
    private int newArrowTime = 225; // when timecount reaches this (this variable's value * timer's delay = time in
                                    // ms) initialize a new arrow
    private String pressed = notPressed; // set to the value of the arrow key that is pressed, used to check which arrow
                                         // should be destroyed
    private ImageIcon inactiveLeft, inactiveDown, inactiveUp, inactiveRight; // default arrow sprites
    private ImageIcon active[] = new ImageIcon[arraySize]; // list of all the arrows

    public Simfile smf; // .csm file to read

    List<List<String>> timestamp; // list of timestamps to make notes appear at the right time
    public int noteindex = 0; // number of arrows initialized
    public File file; // .wav audio file for song
    public Clip music; // actual song clip

    private JLabel judgeLabel = new JLabel();
    private JProgressBar progressBar = new JProgressBar(0, 100);
    private long currentSongLength;

    public int addnoteindex() // literally just for the actionlistener
    {
        noteindex++;
        return 1;
    }

    public PanelPlay(Window owner, int width, int height, Simfile simfile) {
        initTopLayout();
        initProgressBar();

        // Catch all other exceptions
        try {
            this.file = new File(simfile.AudioFile());
            if (file.exists()) {
                AudioInputStream stream = AudioSystem.getAudioInputStream(file);
                this.music = AudioSystem.getClip();
                music.open(stream);
                currentSongLength = music.getMicrosecondLength();
            }
        }
        /******************************************
         * These are all defined in the Simfile class
         ******************************************/
        catch (IOException e) {
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
            // Stops reading the simfile
            tm.stop();
            audioThread.interrupt();
            // Stops playing the song
            songstarted = false;
            music.stop();
            music.flush();
            // Sets the window to show PanelSelect
            SwingUtilities.invokeLater(() -> owner.showView(new PanelSelect(owner, Driver.width, Driver.height)));
        });
        topLayout.backButton.setText(" Exit");
        topLayout.add(Box.createHorizontalGlue());

        judgeLabel.setFont(Driver.fontBold.deriveFont(28f));

        topLayout.add(judgeLabel);
        topLayout.add(Box.createRigidArea(new Dimension(20, 60)));

        topLayout.setVisible(true);
        add(topLayout);
    }
    private void initProgressBar() {
        add(Box.createVerticalGlue());

        progressBar.setPreferredSize(new Dimension(this.getWidth(), 20));
        progressBar.setForeground(new Color(243, 66, 53));
        progressBar.setBorder(new LineBorder(Color.BLUE));
        progressBar.setValue(0);
        add(progressBar);
    }

    // gets called when arrow key is pressed, parameter depends on which arrow key
    private class ArrowAction extends AbstractAction {

        private String direction;

        public ArrowAction(String direction) {
            this.direction = direction;
        }

        // sends a value to pressed based on which key was pressed
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

    private class ReleaseAction extends AbstractAction {

        private String direction;

        public ReleaseAction(String direction) {
            this.direction = direction;
        }

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

    // called every time the timer runs
    public void actionPerformed(ActionEvent e) {
        // Updates the progress bar
        long currentSongPosition = this.music.getMicrosecondPosition();
        int songProgress = (int)((float)currentSongPosition/this.currentSongLength*100);
        progressBar.setValue(songProgress);

        timeCount++;

        // animate all existing arrows
        for (int start = noteindex; start > 0; start--) {
            y[start] -= velY;
            if (y[start] <= -80 && active[start] != null) {
                int judge = this.Judgement(smf, nextArrow, y[nextArrow]);
                displayResults(judge);
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
                    displayResults(judge);
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
                    displayResults(judge);
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
                    displayResults(judge);
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
                    displayResults(judge);
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
            repaint();
            milltime = temptime;
        }

        // redraw the canvas after all is said and done

        if (!songstarted) {
            audioThread = new Thread(new RunAudio(music));
            audioThread.start();
            songstarted = true;
        }

        double micro = (double) music.getMicrosecondPosition();
        double second = micro / 1000000;
        // label1.setText(Double.toString(micro));
        if (second > Double.parseDouble(timestamp.get(noteindex).get(0)) - 1.0) {
            loadArrow(timestamp.get(noteindex).get(1));
            y[noteindex] = 800;

            addnoteindex();
            // System.out.println(Double.toString(second));
            // System.out.println(timestamp.get(noteindex).get(0));
        }

    }

    // DRAW!
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

    // default arrows
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

    // make new arrows
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

    public int Judgement(Simfile sim, int notenum, int y) {
        int diff = Math.abs(y - 80);

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

    public int calcScore() {
        int totalscore = 0;
        for (Integer s : scores)
            totalscore += s;

        return totalscore;
    }

    public double calcAccuracy() {
        int totalscore = calcScore();

        return (double) totalscore / ((double) this.scores.size() * 3.0);
    }

    public void addNote(int score) {
        scores.add(score);
    }

    public void displayResults(int judge) {
        // Updates the judgement text
        switch (judge) {
        case 3:
            judgeLabel.setText("PERFECT");
            judgeLabel.setForeground(new Color(56,142,60));
            break;
        case 2:
            judgeLabel.setText("GREAT");
            judgeLabel.setForeground(new Color(245,124,0));
            break;
        case 1:
            judgeLabel.setText("GOOD");
            judgeLabel.setForeground(new Color(251,192,45));
            break;
        case 0:
            judgeLabel.setText("MISS");
            judgeLabel.setForeground(new Color(211,47,47));
            break;

        }

        // System.out.println(this.calcScore());
        // System.out.println(this.calcAccuracy());
    }

}