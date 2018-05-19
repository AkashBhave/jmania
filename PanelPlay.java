import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelPlay extends JPanel implements ActionListener {

    private int width, height; 
    private int[] x = new int[1000];
    private int[] y = new int[1000];
    private int velY = 2;
    private Window owner;
    private String arrowPath = Driver.projectPath + "/assets/";
    private Timer tm = new Timer(1, this);
    private int left = 0;
    private int down = 1;
    private int up = 2;
    private int right = 3;
    private int arrowCount = 0;
    private int nextArrow = 1;
    private int timeCount;
    private boolean empty = true;
    private boolean pressed = false;
    private ImageIcon inactiveLeft, inactiveDown, inactiveUp, inactiveRight;
    private ImageIcon active[] = new ImageIcon[1000];
    
    public PanelPlay (Window owner, int width, int height) {
        this.owner = owner;
        this.width = width;
        this.height = height;

        y[arrowCount] = 800;
        
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
    
    private class ArrowAction extends AbstractAction {
        
        private int direction;
        
        public ArrowAction (int direction) {
            this.direction = direction;
        }
        
        public void actionPerformed (ActionEvent e) {
            pressed = true;

        }
    }
    
    public void actionPerformed (ActionEvent e) {
        timeCount++;
        if (timeCount % 150 == 0) {
            int random = (int)(Math.random() * 4);
            arrowCount += 1;
            loadArrow(random);
            y[arrowCount] = 800;
        }

        for (int start = arrowCount; start > 0; start --) {
            y[start] -= velY;
        }   

        if (y[nextArrow] < -20 && y[nextArrow] != 0) {
            nextArrow ++;
        }

        if (pressed) {
            if (y[nextArrow] < 500 && active[nextArrow] != null) {
                active[nextArrow] = null;
                nextArrow ++;
            }
            pressed = false;
        }
        System.out.println(String.valueOf(y[nextArrow]));
            
        repaint();
    }
    
    public void paintComponent (Graphics g) {
        
        super.paintComponent(g);
        g.setColor(Driver.bgColor);
        g.fillRect(0, 0, width, height);
        
        loadStaticSprites(g);
        
        
        if (arrowCount >= 1) {
            for (int start = arrowCount; start > 0; start --) {
                if (active[start] != null) {
                    g.drawImage(active[start].getImage(), x[start], y[start], 80, 80, null, null);
                }
            }
        }
        
        tm.start();
    }
    
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
    
    private void loadArrow (int direction) {
        switch (direction) {
            
            case 0 :
            active[arrowCount] = new ImageIcon(arrowPath + "activeLeft.png");
            x[arrowCount] = 425;
            break;
            
            case 1 :
            active[arrowCount] = new ImageIcon(arrowPath + "activeDown.png");
            x[arrowCount] = 525;
            break;
            
            case 2 :
            active[arrowCount] = new ImageIcon(arrowPath + "activeUp.png");
            x[arrowCount] = 625;
            break;
            
            case 3 :
            active[arrowCount] = new ImageIcon(arrowPath + "activeRight.png");
            x[arrowCount] = 725;
            break;

        }
    }

}