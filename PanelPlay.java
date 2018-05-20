import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class PanelPlay extends JPanel implements ActionListener {

    private int width, height; 
    private int[] x = new int[1000]; // list of arrow x-coords
    private int[] y = new int[1000]; // list of arrow y-coords
    private int velY = 3; // speed of each arrow (not completely true, speed is also effected by timer delay)
    private Window owner; // jframe owner
    private String arrowPath = Driver.projectPath + "/assets/images/"; // sprite path
    private Timer tm = new Timer(2, this); // animation timer
    
    // some ints to make life easier
    private final int left = 0;
    private final int down = 1;
    private final int up = 2;
    private final int right = 3;
    private final int notPressed = 4;

    private int arrowCount = 0; // number of arrows initialized
    private int nextArrow = 1; // the topmost arrow to be destroyed
    private int timeCount; // counter for when to initialize a new arrow
    private int newArrowTime = 150; // when timecount reaches this (this variable's value * timer's delay = time in ms) initialize a new arrow
    private int pressed = 4; // set to the value of the arrow key that is pressed, used to check which arrow should be destroyed
    private ImageIcon inactiveLeft, inactiveDown, inactiveUp, inactiveRight; // default arrow sprites
    private ImageIcon active[] = new ImageIcon[1000]; // list of all the arrows
    
    public PanelPlay (Window owner, int width, int height, Simfile simfile) {
        this.owner = owner;
        this.width = width;
        this.height = height;

        y[arrowCount] = 800; // starts first arrow at y-coordinate 800 (80 pixels below bottom of screen)
        
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
        
        private int direction;
        
        public ArrowAction (int direction) {
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
        if (timeCount % newArrowTime == 0) {
            int random = (int)(Math.random() * 4);
            arrowCount += 1;
            loadArrow(random);
            y[arrowCount] = 800;
        }

        // animate all existing arrows
        for (int start = arrowCount; start > 0; start --) {
            y[start] -= velY;
        }   

        // if an arrow goes off the screen, destroy it
        if (y[nextArrow] < -80 && y[nextArrow] != 0) {
            active[nextArrow] = null;
            nextArrow ++;
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

        repaint(); // redraw the canvas after all is said and done

    }
    
    // DRAW!
    public void paintComponent (Graphics g) {
        
        // background
        super.paintComponent(g);
        g.setColor(Driver.bgColor);
        g.fillRect(0, 0, width, height);
        
        loadStaticSprites(g); // draw default arrows
        
        // redraw every arrow with their updated y-coord values (the real animation)
        if (arrowCount >= 1) {
            for (int start = arrowCount; start > 0; start --) {
                if (active[start] != null) {
                    g.drawImage(active[start].getImage(), x[start], y[start], 80, 80, null, null);
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
        
        g.drawImage(inactiveLeft.getImage(), 425, 100, 80, 80, null, null);
        g.drawImage(inactiveDown.getImage(), 525, 100, 80, 80, null, null);
        g.drawImage(inactiveUp.getImage(), 625, 100, 80, 80, null, null);
        g.drawImage(inactiveRight.getImage(), 725, 100, 80, 80, null, null);
        
    }
    
    // make new arrows 
    private void loadArrow (int direction) {
        switch (direction) {
            
            case 0 :
            active[arrowCount] = new ImageIcon(arrowPath + "activeLeft.png");
            active[arrowCount].setDescription("left");
            x[arrowCount] = 425;
            break;
            
            case 1 :
            active[arrowCount] = new ImageIcon(arrowPath + "activeDown.png");
            active[arrowCount].setDescription("down");
            x[arrowCount] = 525;
            break;
            
            case 2 :
            active[arrowCount] = new ImageIcon(arrowPath + "activeUp.png");
            active[arrowCount].setDescription("up");
            x[arrowCount] = 625;
            break;
            
            case 3 :
            active[arrowCount] = new ImageIcon(arrowPath + "activeRight.png");
            active[arrowCount].setDescription("right");
            x[arrowCount] = 725;
            break;

        }
    }

}