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

@SuppressWarnings("serial")

class RunAudio implements Runnable 
{
   private Clip music;
   
   public RunAudio(Clip music)
   {
      this.music = music;
   }
   public void run()
   {
      if (!music.isRunning())
         music.start();
   }
}

public class PanelPlay extends JPanel implements ActionListener {

    private int arraySize = 1000;
    private int width, height; 
    private int[] x = new int[1000]; // list of arrow x-coords
    private int[] y = new int[1000]; // list of arrow y-coords
    private int velY = 1; // speed of each arrow (not completely true, speed is also effected by timer delay)
    private Window owner; // jframe owner
    private String arrowPath = Driver.projectPath + "/assets/images/"; // sprite path
    private Timer tm = new Timer(1, this); // animation timer
    
    // some ints to make life easier
    private final String left = "1000";
    private final String down = "0100";
    private final String up = "0010";
    private final String right = "0001";
    private final String notPressed = "0000";

    private long temptime;

    private long milltime = System.currentTimeMillis();;
    private boolean songstarted = false;

    private int arrowCount = 0; // number of arrows initialized
    private int nextArrow; // the topmost arrow to be destroyed
    private int timeCount; // counter for when to initialize a new arrow
    private int newArrowTime = 225; // when timecount reaches this (this variable's value * timer's delay = time in ms) initialize a new arrow
    private String pressed = notPressed; // set to the value of the arrow key that is pressed, used to check which arrow should be destroyed
    private ImageIcon inactiveLeft, inactiveDown, inactiveUp, inactiveRight; // default arrow sprites
    private ImageIcon active[] = new ImageIcon[1000]; // list of all the arrows

    public Simfile smf = new Simfile("assets/songs/Never_Gonna_Give_You_Up/Never_Gonna_Give_You_Up.csm", 0.0);
    List<List<String>> timestamp = smf.NotesTime();
    public int noteindex = 0;
    public File file;
    public Clip music;

    public int addnoteindex() // literally just for the actionlistener
    {
        noteindex++;
        return 1;
    }
    
    public PanelPlay (Window owner, int width, int height, Simfile simfile) throws Exception {
        try { 
            this.file = new File("assets/songs/Never_Gonna_Give_You_Up/Never_Gonna_Give_You_Up.wav");
            if (file.exists()) {
                AudioInputStream stream = AudioSystem.getAudioInputStream(file);
                this.music = AudioSystem.getClip();
                music.open(stream);
            } 
        }
        catch (IOException e)
        {
            throw new RuntimeException("There was an I/O error reading the audio file.");
        }
        catch (LineUnavailableException e)
        {
            throw new RuntimeException("We messed up, no line is available for the sound.");
        }
        catch (UnsupportedAudioFileException e)
        {
            throw new RuntimeException("This type of audio file is unsupported. Only uncompressed formats are supported.");
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
        imap.put(leftKey, "loadLeft");
        imap.put(rightKey, "loadRight");
        imap.put(downKey, "loadDown");
        imap.put(upKey, "loadUp");
        
        ActionMap amap = this.getActionMap();
        amap.put("loadLeft", new ArrowAction(left));
        amap.put("loadRight", new ArrowAction(right));
        amap.put("loadDown", new ArrowAction(down));
        amap.put("loadUp", new ArrowAction(up));
        
        
        owner.requestFocus();
    }
    
    // gets called when arrow key is pressed, parameter depends on which arrow key
    private class ArrowAction extends AbstractAction {
        
        private String direction;
        
        public ArrowAction (String direction) {
            this.direction = direction;
        }
        
        // sends a value to pressed based on which key was pressed
        public void actionPerformed (ActionEvent e) {
            pressed = direction;

        }
    }
    
    // called every time the timer runs
    public void actionPerformed (ActionEvent e) {

        // if (value of newArrowTime * value of timer's delay) ms has passes, initialize a new arrow randomly
        timeCount++;
        /*if (timeCount % newArrowTime == 0) {
            int random = (int)(Math.random() * 4);
            arrowCount += 1;
            loadArrow(random);
            y[arrowCount] = 800;
        }*/

        // animate all existing arrows
        for (int start = noteindex; start > 0; start --) {
            y[start] -= velY;
            if (y[start] <= -80 && active[start] != null) {
                active[start] = null;
                System.out.println("rekt");
            }
        }   

        for (int start = 1; start < noteindex; start ++) {
            if (active[start] != null) {
                nextArrow = start;
                break;
            }
        }

        // if an arrow key is pressed and the topmost arrow is the corresponding arrow, destroy it.
        if (y[nextArrow] < 500 && active[nextArrow] != null) {
            switch (pressed) {

                case left :
                    if (active[nextArrow].getDescription() == "left") {
                        active[nextArrow] = null;
                        nextArrow ++;
                    } 
                    break;
                case down :
                    if (active[nextArrow].getDescription() == "down") {
                        active[nextArrow] = null;
                        nextArrow ++;
                    } 
                    break;
                case up :
                    if (active[nextArrow].getDescription() == "up") {
                        active[nextArrow] = null;
                        nextArrow ++;
                    } 
                    break;
                case right :
                    if (active[nextArrow].getDescription() == "right") {
                        active[nextArrow] = null;
                        nextArrow ++;
                    } 
                    break;
                case notPressed :
                    break;
            }
            pressed = notPressed;
        }
        temptime = System.currentTimeMillis();



        if (temptime-milltime >= 16) {
            repaint();
            milltime = temptime;
        }

         // redraw the canvas after all is said and done
        
        if (!songstarted) {
            new Thread(new RunAudio(music)).start(); 
            songstarted = true;
        }

        double micro = (double)music.getMicrosecondPosition();
        double second = micro/1000000;
        //label1.setText(Double.toString(micro));
        if (second > Double.parseDouble(timestamp.get(noteindex).get(0))-0.7)
        {
            loadArrow(timestamp.get(noteindex).get(1));
            y[noteindex] = 800;
            
            addnoteindex();
            //System.out.println(Double.toString(second));
            //System.out.println(timestamp.get(noteindex).get(0));
        }

    }
    
    // DRAW!
    public void paintComponent (Graphics g) {
        
        // background
        super.paintComponent(g);
        g.setColor(Driver.bgColor);
        g.fillRect(0, 0, width, height);
        
        loadStaticSprites(g); // draw default arrows
        
        // redraw every arrow with their updated y-coord values (the real animation)
        if (noteindex >= 1) {
            for (int start = noteindex; start > 0; start --) {
                if (active[start] != null) {
                    g.drawImage(active[start].getImage(), x[start], y[start], null, null);
                }
            }
        }
        
        tm.start(); // start animation
    }
    
    // default arrows
    private void loadStaticSprites (Graphics g) {
       
        inactiveDown = new ImageIcon(arrowPath + "inactiveDown.png");
        inactiveLeft = new ImageIcon(arrowPath + "inactiveLeft.png");
        inactiveUp = new ImageIcon(arrowPath + "inactiveUp.png");
        inactiveRight = new ImageIcon(arrowPath + "inactiveRight.png");
        
        g.drawImage(inactiveLeft.getImage(), 425, 100, null, null);
        g.drawImage(inactiveDown.getImage(), 525, 100, null, null);
        g.drawImage(inactiveUp.getImage(), 625, 100, null, null);
        g.drawImage(inactiveRight.getImage(), 725, 100, null, null);
        
    }
    
    // make new arrows 
    private void loadArrow (String direction) {
        switch (direction) {
            
            case left :
            active[noteindex] = new ImageIcon(arrowPath + "activeLeft.png");
            active[noteindex].setDescription("left");
            x[noteindex] = 425;
            break;
            
            case down :
            active[noteindex] = new ImageIcon(arrowPath + "activeDown.png");
            active[noteindex].setDescription("down");
            x[noteindex] = 525;
            break;
            
            case up :
            active[noteindex] = new ImageIcon(arrowPath + "activeUp.png");
            active[noteindex].setDescription("up");
            x[noteindex] = 625;
            break;
            
            case right :
            active[noteindex] = new ImageIcon(arrowPath + "activeRight.png");
            active[noteindex].setDescription("right");
            x[noteindex] = 725;
            break;

            case notPressed :
            break;

        }
    }

}